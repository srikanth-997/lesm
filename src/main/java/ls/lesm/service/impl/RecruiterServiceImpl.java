package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ls.lesm.exception.DateMissMatchException;
import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.exception.SupervisorAlreadyExistException;
import ls.lesm.exception.TicketClosedException;
import ls.lesm.model.Clients;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.exp.ExpenseNotification;
import ls.lesm.model.recruiter.CandidateProfiles;
import ls.lesm.model.recruiter.CandidateStatus;
import ls.lesm.model.recruiter.Consultant;
import ls.lesm.model.recruiter.JobString;
import ls.lesm.model.recruiter.RecruiterProfitOrLoss;
import ls.lesm.model.recruiter.Status;
import ls.lesm.payload.request.ApproveProfilesRequest;
import ls.lesm.payload.request.InterviewScheduleStatusRequest;
import ls.lesm.payload.response.CandidateProfilesResponse;
import ls.lesm.payload.response.EmployeesDropDownResponse;
import ls.lesm.payload.response.JobStringResponse;
import ls.lesm.payload.response.TaggedJobResponse;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.expRepo.ExpenseNotificatonRepo;
import ls.lesm.repository.recruiter.CandidateProfilesRepo;
import ls.lesm.repository.recruiter.CandidateStatusRepo;
import ls.lesm.repository.recruiter.ConsultantRepo;
import ls.lesm.repository.recruiter.JobStringRepo;
import ls.lesm.repository.recruiter.RecruiterProfitOrLossRepo;
import ls.lesm.service.IImageService;
import ls.lesm.service.RecruiterService;

@Service
//@Configuration
@EnableScheduling
@Transactional
public class RecruiterServiceImpl implements RecruiterService {

	@Autowired
	private JobStringRepo jobStringRepo;

	@Autowired
	private IImageService imageService;

	@Autowired
	private ExpenseNotificatonRepo expenseNotificatonRepo;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	private CandidateProfilesRepo candidateProfilesRepo;
	
	@Autowired
	private SubDepartmentsRepository subDepartmentsRepository;
	
	@Autowired
	private DesignationsRepository designationsRepository;
	
	@Autowired
	private CandidateStatusRepo candidateStatusRepo;
	
	@Autowired
	private ConsultantRepo consultantRepo;
	
	@Autowired
	private RecruiterProfitOrLossRepo recruiterProfitOrLossRepo;
	
	@Autowired
	private ClientsRepository clientRepo;
	
	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;

	@Override
	public String postJobString(JobString jobString, Principal principal,
			                     Set<Integer> empIds,MultipartFile file, Integer clientId, Integer hiringTypeId) {
  
		Optional<Clients> client=this.clientRepo.findById(clientId);
		Optional<EmployeeType> hiringType=this.employeeTypeRepository.findById(hiringTypeId);
		if(client.isPresent())
		jobString.setClientId(client.get());
		
		if(hiringType.isPresent())
			jobString.setHiringType(hiringType.get());
		jobString.setStringCreatedBy(principal.getName());
		jobString.setCreatedAt(LocalDate.now());
		jobString.setTicketStatus(true);
		if (jobString.getOpenDate().isAfter(jobString.getCloseDate()))
			throw new DateMissMatchException("Open date should not be before close date");

		// open
		// this is for creating JobString ticket name with meaning full information
		String positionIntoString = jobString.getTotalPosition().toString();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
		String openD = dtf.format(jobString.getOpenDate());
		String closeD = dtf.format(jobString.getCloseDate());
		LocalDateTime timeStamp = LocalDateTime.now();
		String timeStampInString = String.valueOf(timeStamp);
		//if(client.isPresent()) 
		String ticket = client.get().getClientsNames() + "_" + positionIntoString + "_" + openD + "_" + closeD + "_"
				+ jobString.getBudget() + "_" + timeStampInString;
		
		// this jobString ticket name contain jobStrign information
		// eg. copg_05_01.may.2022_05.may.2022_15lpa_currentHours
		jobString.setJobStringTicket(ticket);
		// close
		

		List<MasterEmployeeDetails> employees = this.masterEmployeeDetailsRepository.findAllById(empIds);
		MasterEmployeeDetails jobStringCreaterEmployee = this.masterEmployeeDetailsRepository
				.findByLancesoft(principal.getName());

		jobString.setMasterEmployeeDetails(employees);

		try {
			String fileName = imageService.save(file);
			String imageUrl = imageService.getImageUrl(fileName);
			jobString.setSampleResume(imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// creating a notification people who are tagged this jobString will get
		// notification
		for (MasterEmployeeDetails emp : employees) {
			ExpenseNotification notification = new ExpenseNotification();
			notification.setMessage("Hi " + emp.getFirstName() + " " + emp.getLastName() + ", "
					+ jobStringCreaterEmployee.getFirstName() + " " + jobStringCreaterEmployee.getLastName()
					+ " has sent you new Job String");
			this.masterEmployeeDetailsRepository.findById(emp.getEmpId()).map(id -> {
				notification.setMasterEmployeeDetails(id);
				notification.setFlag(true);
				notification.setCreatedAt(LocalDateTime.now());
				return this.expenseNotificatonRepo.save(notification);
			});
		}

		this.jobStringRepo.save(jobString);

		return ticket;
	}

	//everyday at 11:00 pm this method will execute
	@Scheduled(cron = "0 0 23 * * *")
	@Override // this is job it will execute once every day, it will check all the opened
				// ticket close date, if ticket close date was yasterdays one then it will close
				// that ticket today
	public void closeTicketJob() {
		List<JobString> allOpendJobString = this.jobStringRepo.findAllByTicketStatus(true);
		// taggedEmpsRec=allOpendJobString.stream().map(JobString::getMasterEmployeeDetails).collect(Collectors.toList());

		for (JobString j : allOpendJobString) {
			if (j.getCloseDate().equals(LocalDate.now().minusDays(1))) {// camparing current date minus one day bcz if
																		// today is last date of close means we have
																		// today, ticket should close tomm
				j.setTicketStatus(false);

				MasterEmployeeDetails stringCreateEmp = this.masterEmployeeDetailsRepository// extracting emp who
																							// created this ticket, for
																							// notification
						.findByLancesoft(j.getStringCreatedBy());
				ExpenseNotification notiForCreater = new ExpenseNotification();// setting up notification for ticket
																				// creater
				notiForCreater.setMessage("Hi " + stringCreateEmp.getFirstName() + " " + stringCreateEmp.getLastName()
						+ ", this ticket " + j.getJobStringTicket() + " created by you has been closed");
				notiForCreater.setMasterEmployeeDetails(stringCreateEmp);
				this.expenseNotificatonRepo.save(notiForCreater);
				List<MasterEmployeeDetails> taggedEmpsRec = j.getMasterEmployeeDetails();// extracting all the recrutier
																							// who are tagged for this
																							// jobstring
				for (MasterEmployeeDetails recEmp : taggedEmpsRec) {
					ExpenseNotification noti = new ExpenseNotification();// setting up noti for recruiter
					noti.setMessage("Hi " + recEmp.getFirstName() + " " + recEmp.getLastName() + ", this ticket "
							+ j.getJobStringTicket() + " has been closed");
					noti.setMasterEmployeeDetails(recEmp);
					noti.setFlag(true);
					noti.setCreatedAt(LocalDateTime.now());
					this.expenseNotificatonRepo.save(noti);
				}

				this.jobStringRepo.save(j);
			}
		}

	}

	@Override
	public List<JobString> getOpenedJobStringByLoggedInEmp(Principal principal) {
		
		MasterEmployeeDetails loggedInEmp=this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
		
		List<JobString> allTaggedJobString=this.jobStringRepo.findAllByMasterEmployeeDetailsAndTicketStatus(loggedInEmp,  true);
		return allTaggedJobString;
	}

	@Override
	public String sendProfiles(CandidateProfiles profiles, Integer jobStringId, Principal principal,
			MultipartFile resume) {

		profiles.setCreatedAt(LocalDateTime.now());
		profiles.setManagerApproval(Status.PENDING);
		
		try {//sending file to firebase bucket
			String fileName = imageService.save(resume);
			String resumeUrl = imageService.getImageUrl(fileName);
			System.out.println("========================="+resumeUrl);
			profiles.setCandiResume(resumeUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//setting jobString id to this profile 
		Optional<JobString> jobString = this.jobStringRepo.findById(jobStringId);
		if(jobString.get().isTicketStatus()==true) //checking ticket is opened or closed when it is opend then only allowing to send profiles
			profiles.setJobString(jobString.get());
		else 
			throw new TicketClosedException("This Ticket (" +jobString.get().getJobStringTicket()+ ") has been closed on "+jobString.get().getCloseDate());
		
		MasterEmployeeDetails loggedInEmp = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
		profiles.setMasterEmployeeDetails(loggedInEmp);//setting recruiter emp id for thid profile
		CandidateProfiles profile = this.candidateProfilesRepo.save(profiles);

		//finding a jobString creater emp for sending notification about new profile
		MasterEmployeeDetails stringCreaterEmp = this.masterEmployeeDetailsRepository
				                                     .findByLancesoft(jobString.get().getStringCreatedBy());
		
		ExpenseNotification notification = new ExpenseNotification();
		notification.setMessage("Hi " + stringCreaterEmp.getFirstName() + " " + stringCreaterEmp.getLastName() + ", "
				+ loggedInEmp.getFirstName() + " " + loggedInEmp.getLastName()
				+ " has sent you new profile for this ticket (" + jobString.get().getJobStringTicket()+")");
		notification.setMasterEmployeeDetails(stringCreaterEmp);
		notification.setFlag(true);
		notification.setCreatedAt(LocalDateTime.now());
		this.expenseNotificatonRepo.save(notification);

		return profile.getCandidateId();

	}

	@Override
	public List<EmployeesDropDownResponse> recruitersDropDown(Principal principal) {
		Designations desg=this.designationsRepository.findByDesgNamesLike("Recruiter");
		if(desg==null)
			throw new RecordNotFoundException("Recruiter designation not exist, please add designation as (Recruiter)");
		MasterEmployeeDetails supervisor=this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
		List<MasterEmployeeDetails> recruiters=this.masterEmployeeDetailsRepository.findByDesignationsAndSupervisor(desg,supervisor);
		List<EmployeesDropDownResponse> list=new ArrayList<EmployeesDropDownResponse>();
		for(MasterEmployeeDetails rec: recruiters) {
			EmployeesDropDownResponse dropDownRes=new EmployeesDropDownResponse();
			list.add(dropDownRes);
			dropDownRes.setEmpId(rec.getEmpId());
			dropDownRes.setLancesoftId(rec.getLancesoft());
			dropDownRes.setName(rec.getFirstName()+" "+rec.getLastName());
		}
	
		return list;
	}

	@Override
	public void managerProfileApproval(List<ApproveProfilesRequest> req) {
		for(ApproveProfilesRequest r:req) {

			CandidateProfiles profile=this.candidateProfilesRepo.findById(r.getId()).get();

			//ExpenseNotification noti=new ExpenseNotification();
			//noti.setMessage();
			profile.setManagerApproval(r.getStatus());
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MMMM.yyyy hh:mm:ss", Locale.ENGLISH);
//			String approveAt=dtf.format(LocalDateTime.now());
			profile.setApproveAt(LocalDateTime.now());
			CandidateProfiles savedProfile=this.candidateProfilesRepo.save(profile);
		
			Optional<CandidateStatus> savedCandidateRecord=this.candidateStatusRepo.findByCandidateProfiles(profile);
			if(savedProfile.getManagerApproval().equals(Status.APPROVE)) {
				
				
				if(savedCandidateRecord.isEmpty()) {
				CandidateStatus interviewStatus=new CandidateStatus();
				interviewStatus.setCandidateProfiles(profile);
				interviewStatus.setMasterEmployeeDetails(profile.getMasterEmployeeDetails());
				interviewStatus.setJobString(profile.getJobString());
				interviewStatus.setL1Status(Status.PENDING);
				interviewStatus.setL2Status(Status.PENDING);
				this.candidateStatusRepo.save(interviewStatus);
				}
			}
		}
				

	}
	@Override
	public InterviewScheduleStatusRequest scheduleInterview(String condidateProfileId,Principal principal) {
		InterviewScheduleStatusRequest schedule=new InterviewScheduleStatusRequest();
		Optional<CandidateProfiles> candidateProfile= this.candidateProfilesRepo.findById(condidateProfileId);
		
	  Optional<CandidateStatus> candidateStatus=	this.candidateStatusRepo.findByCandidateProfiles(candidateProfile.get());
	  
	          if(candidateStatus.isPresent()) {
	        	  
	        	 schedule.setCandiName(StringUtils.capitalize(candidateStatus.get().getCandidateProfiles().getCandidateName()));        	  
	        	 schedule.setCondiProfileId(condidateProfileId);
	        	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	        	 
	        	 schedule.setJobStringId(candidateStatus.get().getJobString().getJobStringId());
	        	 schedule.setJoined(candidateStatus.get().isJoined());
	        	 if(candidateStatus.get().getL1PostponedAt()!=null)
	        	 schedule.setL1PostponedAt(candidateStatus.get().getL1PostponedAt().format(formatter));
	        	 if(candidateStatus.get().getL1ScheduleAt()!=null)
	        	 schedule.setL1ScheduleAt(candidateStatus.get().getL1ScheduleAt().format(formatter));
	        	 
	        	 schedule.setL1Status(candidateStatus.get().getL1Status());
	        	 if(candidateStatus.get().getL2PostponedAt()!=null)
	        	 schedule.setL2PostponedAt(candidateStatus.get().getL2PostponedAt().format(formatter));
	        	 if(candidateStatus.get().getL2ScheduleAt()!=null)
	        	 schedule.setL2ScheduleAt(candidateStatus.get().getL2ScheduleAt().format(formatter));
	        	 schedule.setL2Status(candidateStatus.get().getL2Status());
	        	 schedule.setRecId(candidateStatus.get().getMasterEmployeeDetails().getLancesoft());
	        	 
	        	 schedule.setRecruiterName(StringUtils.capitalize(candidateStatus.get().getMasterEmployeeDetails().getFirstName()+" "+
	        	 candidateStatus.get().getMasterEmployeeDetails().getLastName()));
	        	 
	        	 schedule.setReleasedOffer(candidateStatus.get().isReleasedOffer());
	        	 schedule.setReleasedOfferAt(candidateStatus.get().getReleasedOfferAt());
	        	 schedule.setStatusId(candidateStatus.get().getStatusId());
	        	 schedule.setProfileApproval(candidateProfile.get().getManagerApproval());
	        	 
	        	 return schedule;
	            }else {
	            	throw new SupervisorAlreadyExistException("The profile is rejected or pending");
			
	            }

   
	}

	@Override
	public List<RecruiterProfitOrLoss> getRecruiterProfitOrLoss( Principal principal) {
		//this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName())
		//int[] ids= {147,148};
		//this.masterEmployeeDetailsRepository
		MasterEmployeeDetails supEmp=this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
		
		List<MasterEmployeeDetails> allRec=this.masterEmployeeDetailsRepository.findBySupervisorAndDesignations(supEmp,this.designationsRepository.findByDesgNames("Recruiter"));
		
	List<Consultant> consultans=this.consultantRepo.findByMasterEmployeeDetailsIn(allRec);
		for(Consultant c:consultans) {
			RecruiterProfitOrLoss rec =new RecruiterProfitOrLoss();
			rec.setMasterEmployeeDetails(c.getMasterEmployeeDetails());
			rec.setProfitOrLoss(8.33*c.getSalary()/100);
			this.recruiterProfitOrLossRepo.save(rec);
		}
		
		return this.recruiterProfitOrLossRepo.findByMasterEmployeeDetailsIn(allRec);
		//return null;
	}

	@Override
	public List<JobStringResponse> getPostedJobStringByLoggedIn(Principal principal, boolean flag) {

		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());

		List<JobString> js = this.jobStringRepo.findByStringCreatedByAndTicketStatus(employee.getLancesoft(), flag);

		return sendJobStringFormatted(js);
	}
//	@Override
//	public	List<JobStringResponse> getPostedJobStringByLoggedInAndFillters(Principal principal, boolean flag, LocalDate date, Integer hiringTypeId, Integer clientId){
//		
//		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
//		
//		   Optional<EmployeeType> hiringType=this.employeeTypeRepository.findById(hiringTypeId);
//		Optional<Clients> client=this.clientRepo.findById(clientId);
//		
//		List<JobString> js = this.jobStringRepo.findByStringCreatedByAndTicketStatusOrCreatedAtOrHiringTypeOrClientId(employee.getLancesoft(), flag,date, hiringType.get(),client.get());
//		return sendJobStringFormatted(js);
//		
//	}
	
	
	@Override
	public List<JobStringResponse> getPostedJobStringByLoggedInAndFillters(
	        Principal principal, boolean flag, LocalDate date, Integer hiringTypeId, Integer clientId) {
	    
	    MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());

	    final Optional<EmployeeType> hiringType = hiringTypeId != null ? this.employeeTypeRepository.findById(hiringTypeId) : Optional.empty();
	    final Optional<Clients> client = clientId != null ? this.clientRepo.findById(clientId) : Optional.empty();

	    List<JobString> js = jobStringRepo.findAll((Specification<JobString>) (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();
	        
	        // Filter by createdBy and TicketStatus (these are mandatory)
	        predicates.add(cb.equal(root.get("stringCreatedBy"), employee.getLancesoft()));
	        predicates.add(cb.equal(root.get("ticketStatus"), flag));
	        
	        // Filter by createdAt date if provided
	        if (date != null) {
	            // Calculate the date range based on the provided date
	            LocalDate today = LocalDate.now();
	            
	            // Filter all results from last week to today's date
	            if (date.isAfter(today.minusWeeks(1))) {
	                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), today.minusWeeks(1)));
	            }
	            
	            // Filter all results from last month to today's date
	            else if (date.isAfter(today.minusMonths(1))) {
	                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), today.minusMonths(1)));
	            }
	            
	            // If it's neither last week nor last month, just filter by the provided date
	            else {
	                predicates.add(cb.equal(root.get("createdAt"), date));
	            }
	        }
	        
	        // Filter by hiringType if provided
	        if (hiringType.isPresent()) {
	            predicates.add(cb.equal(root.get("hiringType"), hiringType.get()));
	        }
	        
	        // Filter by clientId if provided
	        if (client.isPresent()) {
	            predicates.add(cb.equal(root.get("clientId"), client.get()));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    });
	    
	    return sendJobStringFormatted(js);
	}

	
	
	private List<JobStringResponse> sendJobStringFormatted(List<JobString> js) {
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd,MMMM,yyyy", Locale.ENGLISH);
	    List<JobStringResponse> listRes = new ArrayList<>();

	    for (JobString j : js) {
	        MasterEmployeeDetails employee = masterEmployeeDetailsRepository.findByLancesoft(j.getStringCreatedBy());
	        if (employee == null) {
	            // Handle null employee
	            continue;
	        }

	        List<TaggedJobResponse> taggList = j.getMasterEmployeeDetails().stream()
	                .map(m -> {
	                    TaggedJobResponse tr = new TaggedJobResponse();
	                    tr.setEmpId(m.getEmpId());
	                    tr.setLancesoftId(m.getLancesoft());

	                    String firstName = m.getFirstName();
	                    String lastName = m.getLastName();
	                    String employeeName = "";
	                    if (firstName != null && lastName != null) {
	                        employeeName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1) + " " +
	                                Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	                    } else if (firstName != null) {
	                        employeeName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
	                    } else if (lastName != null) {
	                        employeeName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	                    }
	                    tr.setEmployeeName(employeeName);

	                    return tr;
	                })
	                .collect(Collectors.toList());

	        JobStringResponse jsResponse = new JobStringResponse();
	        jsResponse.setBudget(j.getBudget());
	        jsResponse.setClientName(j.getClientId().getClientsNames());
	        jsResponse.setCloseDate(dtf.format(j.getCloseDate()));
	        jsResponse.setJD(j.getJD());
	        if(j.getHiringType()!=null)
	        jsResponse.setHiringType(j.getHiringType().getTypeName());
	        jsResponse.setJobStringId(j.getJobStringId());
	        jsResponse.setJobStringTicket(j.getJobStringTicket());
	        jsResponse.setOpenDate(dtf.format(j.getOpenDate()));
	        jsResponse.setSampleResume(j.getSampleResume());

	        String firstName = employee.getFirstName();
	        String lastName = employee.getLastName();
	        String createdBy = "";
	        if (firstName != null && lastName != null) {
	            createdBy = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1) + " " +
	                    Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	        } else if (firstName != null) {
	            createdBy = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
	        } else if (lastName != null) {
	            createdBy = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	        }
	        jsResponse.setStringCreatedBy(createdBy);

	        jsResponse.setTicketStatus(j.isTicketStatus());
	        jsResponse.setTotalPosition(j.getTotalPosition());

	        jsResponse.setTaggedEmployees(taggList);
	        listRes.add(jsResponse);
	    }

	    return listRes;
	}


		@Override
		public List<JobStringResponse> getTaggedJobString(Principal principal, boolean flag) {

			MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
			List<JobString> js = this.jobStringRepo.findByMasterEmployeeDetailsAndTicketStatus(employee, flag);
			return sendJobStringFormatted(js);
		}

		@Override
		public List<CandidateProfilesResponse> getSentProfilesByJob(Principal principal, int jobStringId) {
			MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());
			Optional<JobString> jobString=this.jobStringRepo.findById(jobStringId);
			List<CandidateProfiles> profiles=this.candidateProfilesRepo.findByMasterEmployeeDetailsAndJobString(employee,jobString.get());

					
			return formatedCandidateProfiles(profiles);
		}
		
		private List<CandidateProfilesResponse> formatedCandidateProfiles(List<CandidateProfiles> profiles){
			
			DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd,MMMM,yyyy : hh:mm a",Locale.ENGLISH);
			List<CandidateProfilesResponse> listRes=new ArrayList<CandidateProfilesResponse>();
						
			for(CandidateProfiles c: profiles) {
				CandidateProfilesResponse res=new CandidateProfilesResponse();
				if(c.getApproveAt()!=null)
				res.setApproveAt(dtf.format(c.getApproveAt()));
				res.setCandidateId(c.getCandidateId());
				res.setCandidateName(c.getCandidateName());
				res.setCandiResume(c.getCandiResume());
				res.setCurrentCTC(c.getCurrentCTC());
				res.setCurrentOrg(c.getCurrentOrg());
				res.setEmailId(c.getEmailId());
				res.setExpectedCTC(c.getExpectedCTC());
				res.setManagerApproval(c.getManagerApproval());
				res.setMobileNo(c.getMobileNo());
				res.setRelevantExp(c.getRelevantExp());
				res.setSentAt(dtf.format(c.getCreatedAt()));
				res.setTotalExp(c.getTotalExp());
				res.setTicketName(c.getJobString().getJobStringTicket());
				res.setSentBy(StringUtils.capitalize(c.getMasterEmployeeDetails().getFirstName())
						+" "+StringUtils.capitalize(c.getMasterEmployeeDetails().getLastName())
						+" ("+c.getMasterEmployeeDetails().getLancesoft()+")");
				listRes.add(res);
			}
					
			return listRes;
		}

		@Override
		public List<CandidateProfilesResponse> getPostedJobProfilesByJobStringId(int jobStringId) {
			
			Optional<JobString> jobString=this.jobStringRepo.findById(jobStringId);
			
			List<CandidateProfiles> profiles=this.candidateProfilesRepo.findByJobString(jobString.get());
			
			return formatedCandidateProfiles(profiles);
		}

		@Override
		public void updateInterviewStatus(InterviewScheduleStatusRequest schedule) {
			Optional<CandidateProfiles> candidateProfile = this.candidateProfilesRepo.findById(schedule.getCondiProfileId());

			Optional<CandidateStatus> candidateStatus = this.candidateStatusRepo
					.findByCandidateProfiles(candidateProfile.get());
			if(candidateStatus.isPresent()) {
				  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
				 
				 //   LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

				  if(schedule.getL1ScheduleAt()!=null)
				candidateStatus.get().setL1ScheduleAt(LocalDateTime.parse(schedule.getL1ScheduleAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				  
				  if(schedule.getL1PostponedAt()!=null)
				candidateStatus.get().setL1PostponedAt(LocalDateTime.parse(schedule.getL1PostponedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				candidateStatus.get().setL1Status(schedule.getL1Status());
				
				if(schedule.getL2ScheduleAt()!=null)
				candidateStatus.get().setL2ScheduleAt(LocalDateTime.parse(schedule.getL2ScheduleAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				if(schedule.getL2PostponedAt()!=null)
				candidateStatus.get().setL2PostponedAt(LocalDateTime.parse(schedule.getL2PostponedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				candidateStatus.get().setL2Status(schedule.getL2Status());
				candidateStatus.get().setJoined(schedule.isJoined());
				candidateStatus.get().setReleasedOffer(schedule.isReleasedOffer());
				candidateStatus.get().setReleasedOfferAt(schedule.getReleasedOfferAt());
				CandidateStatus savedCandidateStatus= this.candidateStatusRepo.save(candidateStatus.get());
				
				boolean offerStatus= savedCandidateStatus.isReleasedOffer();
				if(offerStatus==true) {
					Consultant consultant=new Consultant();
							consultant.setConId(savedCandidateStatus.getCandidateProfiles().getCandidateId());
							consultant.setConsultantName(savedCandidateStatus.getCandidateProfiles().getCandidateName());
							consultant.setMasterEmployeeDetails(savedCandidateStatus.getMasterEmployeeDetails());
							this.consultantRepo.save(consultant);
					
				}
			}
			
		}
		
		@Override
		public Map<String,Long> sentProfilesTracking(Integer jobId,Principal principal){
			
			List<CandidateProfilesResponse>  allProfiles= getSentProfilesByJob( principal,  jobId);
			
			Long totalApprovedProfiles=allProfiles.stream().filter(p -> p.getManagerApproval().equals(Status.APPROVE)).count();
			Long totalRejectedProfiles=allProfiles.stream().filter(p -> p.getManagerApproval().equals(Status.REJECT)).count();
			Long totalPendingProfiles=allProfiles.stream().filter(p -> p.getManagerApproval().equals(Status.PENDING)).count();
			Integer totalProfiels=allProfiles.size();
			
			Map<String,Long> profileData=new HashMap<String, Long>();
			profileData.put("Approved", totalApprovedProfiles);
			profileData.put("Rejected", totalRejectedProfiles);
			profileData.put("Pending", totalPendingProfiles);
			profileData.put("Total", totalProfiels.longValue());
			
			return profileData;
		}
		
		
}
