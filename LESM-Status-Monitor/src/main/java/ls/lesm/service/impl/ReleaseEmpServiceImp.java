package ls.lesm.service.impl;



import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.DateMissMatchException;

import ls.lesm.exception.EmptyInputException;
import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.model.History;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ReleaseEmpDetails;
import ls.lesm.model.User;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.enums.ReleaseStatus;
import ls.lesm.model.enums.UpdatedStatus;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.UserRepository;
@Service
public class ReleaseEmpServiceImp {

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	ReleaseEmpDetailsRepository releaseEmpDetailsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HistoryRepository historyOfEmpRepository;

	// get all employees
	public synchronized List<MasterEmployeeDetails> getAllEmp(int designation) {
		List<MasterEmployeeDetails> masterEmployeeDetails = masterEmployeeDetailsRepository.findBydesignations_Id(designation);

		if (masterEmployeeDetails.isEmpty())
			throw new RecordNotFoundException("Hey list completely empty, we have nothing to return");
		else {

			List<MasterEmployeeDetails> filter = new ArrayList<MasterEmployeeDetails>();

			for (MasterEmployeeDetails m : masterEmployeeDetails) {

				if (m.getStatus() != EmployeeStatus.EXIT) {

					Optional<ReleaseEmpDetails> rel = releaseEmpDetailsRepository
							.findBymasterEmployeeDetails_Id(m.getEmpId());

					ReleaseEmpDetails releaseEmpDetails = null;

					if (rel.isPresent()) {
						releaseEmpDetails = rel.get();

						if (releaseEmpDetails.getReleaseStatus() != ReleaseStatus.ONHOLD) {

							filter.add(m);
						}

					} else {

						filter.add(m);
					}

				}

			}
			return filter;
		}

	}

	// select an employee for releasing
	public synchronized MasterEmployeeDetails get(String lancesoftId) {
		

			
		MasterEmployeeDetails employee = masterEmployeeDetailsRepository.findByLancesoft(lancesoftId);
         if(employee==null)
         {
        	 throw new EmptyInputException("given employee id is not present");
         }
         
         else
         {
        	 MasterEmployeeDetails employee1 = masterEmployeeDetailsRepository.findById(employee.getEmpId()).get();
			ReleaseEmpDetails releaseEmpDetails = new ReleaseEmpDetails();
			releaseEmpDetails.setMasterEmployeeDetailsId(employee);
			releaseEmpDetails.setMasterEmployeeDetailssupervisor(employee.getSupervisor());
			releaseEmpDetails.setReleaseStatus(ReleaseStatus.ONHOLD);
			releaseEmpDetailsRepository.save(releaseEmpDetails);

			MasterEmployeeDetails m = employee.getSupervisor();

			System.out.println("-------------------------------" + m);

			return m;
		

         }

	}

	public synchronized void approveRequest(String lancesoftId, boolean empstatus, Principal principal) {
		
		
		MasterEmployeeDetails trasferEmp = masterEmployeeDetailsRepository.findByLancesoft(lancesoftId);

		User loggedU = this.userRepository.findByUsername(principal.getName());

		String id = loggedU.getUsername();
		MasterEmployeeDetails updatedBy = this.masterEmployeeDetailsRepository.findByLancesoft(id);

		// MasterEmployeeDetails trasferEmp =
		// masterEmployeeDetailsRepository.findById(emp).get();
		

		MasterEmployeeDetails masterEmployeeDetails = masterEmployeeDetailsRepository.findById(trasferEmp.getEmpId()).get();

		ReleaseEmpDetails details = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(trasferEmp.getEmpId()).get();

		System.out.println(details);

		if (empstatus == true)

		{

			History historyEmp = new History(masterEmployeeDetails.getLancesoft(), masterEmployeeDetails.getFirstName(),
					masterEmployeeDetails.getLastName(), masterEmployeeDetails.getJoiningDate(),
					masterEmployeeDetails.getDOB(), masterEmployeeDetails.getLocation(),
					masterEmployeeDetails.getGender(), masterEmployeeDetails.getEmail(),
					masterEmployeeDetails.getCreatedAt(), masterEmployeeDetails.getVertical(),
					masterEmployeeDetails.getStatus(), masterEmployeeDetails.getAge(),
					masterEmployeeDetails.getIsInternal(), masterEmployeeDetails.getPhoneNo(),
					masterEmployeeDetails.getCreatedBy(), 
					masterEmployeeDetails.getSubDepartments(), masterEmployeeDetails.getDepartments(),
					masterEmployeeDetails.getDesignations(), masterEmployeeDetails.getSupervisor(),
					masterEmployeeDetails.getEmployeeType(), UpdatedStatus.RELEASED, LocalDate.now(), updatedBy);

			historyOfEmpRepository.save(historyEmp);

			details.setReleaseStatus(ReleaseStatus.APPROVED);
			details.setReleaseDate(LocalDate.now());
			releaseEmpDetailsRepository.save(details);
			masterEmployeeDetails.setStatus(EmployeeStatus.EXIT);

			masterEmployeeDetailsRepository.save(masterEmployeeDetails);
		User u = userRepository.findByUsername(masterEmployeeDetails.getLancesoft());
		
		
		
		if(u!=null)
		{
			userRepository.delete(u);
			
		}

			

		} else {


	           if (empstatus == false)
	                details.setReleaseStatus(ReleaseStatus.DENIED);



	           releaseEmpDetailsRepository.delete(details);
	        }
	    }

	public  synchronized String releaseEmployeeDetailss(String oldemp, String newemp) {


		MasterEmployeeDetails trasferEmp1 = masterEmployeeDetailsRepository.findByLancesoft(oldemp);
		
		MasterEmployeeDetails trasferEmp2 = masterEmployeeDetailsRepository.findByLancesoft(newemp);
		List<MasterEmployeeDetails> g = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(trasferEmp1.getEmpId());
		MasterEmployeeDetails newemployee = masterEmployeeDetailsRepository.findById(trasferEmp2.getEmpId()).get();
		

		for (MasterEmployeeDetails m : g) {
			System.out.println(m);

			m.setSupervisor(newemployee);

			masterEmployeeDetailsRepository.save(m);

		}
		return "success";
	}
	
	//release emp service
    public synchronized List<MasterEmployeeDetails> getonholdEmpls(Principal principal) {



       User loggedU = this.userRepository.findByUsername(principal.getName());

System.out.println("................................"+  loggedU);

       String id = loggedU.getUsername();
       System.out.println("................................"+  id);



       MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(id);
       System.out.println("................................"+  employee);



       int empid = employee.getEmpId();

       System.out.println("................................"+  empid);


       List<MasterEmployeeDetails> l1 = new ArrayList<>();
      // List<ReleaseEmpDetails> l1 = new ArrayList<>();

       System.out.println("................................"+ l1);


       List<ReleaseEmpDetails> employees = releaseEmpDetailsRepository.findsBymasterEmployeeDetails_Id(empid);

       System.out.println("................................"+  employees);


       if (employees.isEmpty())
            throw new RecordNotFoundException("Hey list completely empty, we have nothing to return");
        else {



           for (ReleaseEmpDetails r : employees) {



               if (r.getReleaseStatus() == ReleaseStatus.ONHOLD) {



//                   MasterEmployeeDetails onhold_employees = masterEmployeeDetailsRepository.findById(r.getMasterEmployeeDetailsId().getEmpId())
//                            .get();
                   
                   
                    l1.add(r.getMasterEmployeeDetailsId());



               }



           }



           return l1;
        }



   }
    
    
    
    
    
    

}

