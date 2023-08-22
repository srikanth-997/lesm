

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
import ls.lesm.exception.RecordAlredyExistException;
import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.model.Designations;
import ls.lesm.model.History;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ReleaseEmpDetails;
import ls.lesm.model.User;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.enums.ReleaseStatus;
import ls.lesm.model.enums.UpdatedStatus;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.UserRepository;

@Service
public class AbscondEmpServiceImpl {

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	ReleaseEmpDetailsRepository releaseEmpDetailsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HistoryRepository historyOfEmpRepository;

	@Autowired
	DesignationsRepository designationsRepository;

	public List<Designations> GetAllDesignation() {
		List<Designations> designations = designationsRepository.findAll();

		return designations;
	}

	public List<MasterEmployeeDetails> GetEmp(int designation) {

 

        List<MasterEmployeeDetails> masterEmployeeDetails = masterEmployeeDetailsRepository
                .findBydesignations_Id(designation);

 

        if (masterEmployeeDetails.isEmpty()) {

 

            throw new RecordNotFoundException("No records are found");
        } else {

 

            List<MasterEmployeeDetails> filter = new ArrayList<MasterEmployeeDetails>();

 

            for (MasterEmployeeDetails m : masterEmployeeDetails) {
                if (m.getStatus() != EmployeeStatus.EXIT && m.getStatus() != EmployeeStatus.ABSCONDED
                   && m.getStatus() != EmployeeStatus.TERMINATED) {
                    filter.add(m);
                }

 

            }
            return filter;
        }
    }
	public synchronized MasterEmployeeDetails AbscondEmp(LocalDate releaseDate, String lancesoftId, Principal principal) {

		 

        MasterEmployeeDetails abscondedEmp = masterEmployeeDetailsRepository.findByLancesoft(lancesoftId);
        User loggedU = this.userRepository.findByUsername(principal.getName());

 

        String id = loggedU.getUsername();
        MasterEmployeeDetails updatedBy = this.masterEmployeeDetailsRepository.findByLancesoft(id);

 

        MasterEmployeeDetails masterEmployeeDetails = masterEmployeeDetailsRepository.findById(abscondedEmp.getEmpId())
                .get();

 

        Optional<ReleaseEmpDetails> rel = releaseEmpDetailsRepository
                .findBymasterEmployeeDetails_Id(abscondedEmp.getEmpId());
        
        
        

        
        
       
       
         if (masterEmployeeDetails == null) {
            throw new EmptyInputException("given employee id is not present");
        } 
       
            if (rel.isPresent()) {

 

                throw new RecordAlredyExistException("record already absconded", "66");
            } else {

 

                ReleaseEmpDetails releaseEmpDetails = new ReleaseEmpDetails();
                releaseEmpDetails.setMasterEmployeeDetailsId(abscondedEmp);
                releaseEmpDetails.setMasterEmployeeDetailssupervisor(abscondedEmp.getSupervisor());
                releaseEmpDetails.setReleaseStatus(ReleaseStatus.APPROVED);
                releaseEmpDetails.setReleaseDate(releaseDate);
                releaseEmpDetailsRepository.save(releaseEmpDetails);

 

            
 

                History historyEmp = new History(masterEmployeeDetails.getLancesoft(),
                        masterEmployeeDetails.getFirstName(), masterEmployeeDetails.getLastName(),
                        masterEmployeeDetails.getJoiningDate(), masterEmployeeDetails.getDOB(),
                        masterEmployeeDetails.getLocation(), masterEmployeeDetails.getGender(),
                        masterEmployeeDetails.getEmail(), masterEmployeeDetails.getCreatedAt(),
                        masterEmployeeDetails.getVertical(), masterEmployeeDetails.getStatus(),
                        masterEmployeeDetails.getAge(), masterEmployeeDetails.getIsInternal(),
                        masterEmployeeDetails.getPhoneNo(), masterEmployeeDetails.getCreatedBy(),
                        masterEmployeeDetails.getSubDepartments(), masterEmployeeDetails.getDepartments(),
                        masterEmployeeDetails.getDesignations(), masterEmployeeDetails.getSupervisor(),
                        masterEmployeeDetails.getEmployeeType(), UpdatedStatus.ABSCONDED, releaseDate, updatedBy);
                historyOfEmpRepository.save(historyEmp);

 

                masterEmployeeDetails.setStatus(EmployeeStatus.ABSCONDED);

 

                masterEmployeeDetailsRepository.save(masterEmployeeDetails);

 

            }

 

            User u = userRepository.findByUsername(abscondedEmp.getLancesoft());

 

            if (u != null) {
                userRepository.delete(u);

 

            }
            return masterEmployeeDetails;
        }
    
	 public synchronized MasterEmployeeDetails terminateEmp(LocalDate releaseDate,String lancesoftId, Principal principal) {

		 

	        MasterEmployeeDetails terminateEmp = masterEmployeeDetailsRepository.findByLancesoft(lancesoftId);
	        User loggedU = this.userRepository.findByUsername(principal.getName());

	 

	        String id = loggedU.getUsername();
	        MasterEmployeeDetails updatedBy = this.masterEmployeeDetailsRepository.findByLancesoft(id);

	 

	        MasterEmployeeDetails masterEmployeeDetails = masterEmployeeDetailsRepository.findById(terminateEmp.getEmpId())
	                .get();

	 

	        Optional<ReleaseEmpDetails> rel = releaseEmpDetailsRepository
	                .findBymasterEmployeeDetails_Id(terminateEmp.getEmpId());
	        
	        
     
	 

	        if (masterEmployeeDetails == null) {
	            throw new EmptyInputException("given employee id is not present");
	        }
	        
	        
	        
	        
	        
	        
	        
	        
	      
	 

	        else {
	            if (rel.isPresent()) {

	 

	                throw new RecordAlredyExistException("record already terminted", "77");
	            } else {

	 

	                ReleaseEmpDetails releaseEmpDetails = new ReleaseEmpDetails();
	                releaseEmpDetails.setMasterEmployeeDetailsId(terminateEmp);
	                releaseEmpDetails.setMasterEmployeeDetailssupervisor(terminateEmp.getSupervisor());
	                releaseEmpDetails.setReleaseStatus(ReleaseStatus.APPROVED);
	               releaseEmpDetails.setReleaseDate(releaseDate);
	                releaseEmpDetailsRepository.save(releaseEmpDetails);

	 

	 

	                History historyEmp = new History(masterEmployeeDetails.getLancesoft(),
	                        masterEmployeeDetails.getFirstName(), masterEmployeeDetails.getLastName(),
	                        masterEmployeeDetails.getJoiningDate(), masterEmployeeDetails.getDOB(),
	                        masterEmployeeDetails.getLocation(), masterEmployeeDetails.getGender(),
	                        masterEmployeeDetails.getEmail(),masterEmployeeDetails.getCreatedAt(),
	                        masterEmployeeDetails.getVertical(), masterEmployeeDetails.getStatus(),
	                        masterEmployeeDetails.getAge(), masterEmployeeDetails.getIsInternal(),
	                        masterEmployeeDetails.getPhoneNo(), masterEmployeeDetails.getCreatedBy(),
	                        masterEmployeeDetails.getSubDepartments(), masterEmployeeDetails.getDepartments(),
	                        masterEmployeeDetails.getDesignations(), masterEmployeeDetails.getSupervisor(),
	                        masterEmployeeDetails.getEmployeeType(), UpdatedStatus.TERMINATED, LocalDate.now(), updatedBy);
	                historyOfEmpRepository.save(historyEmp);

	 

	                masterEmployeeDetails.setStatus(EmployeeStatus.TERMINATED);

	 

	                masterEmployeeDetailsRepository.save(masterEmployeeDetails);

	 

	            }
	        

	 

	            User u = userRepository.findByUsername(terminateEmp.getLancesoft());

	 

	            if (u != null) {
	                userRepository.delete(u);

	 

	            }
	            return masterEmployeeDetails;
	        }
	    }
	 public synchronized MasterEmployeeDetails releaseEmp( LocalDate releaseDate,String lancesoftId, Principal principal) {

		 System.out.println(releaseDate);

	        MasterEmployeeDetails relaseEmp = masterEmployeeDetailsRepository.findByLancesoft(lancesoftId);
	        User loggedU = this.userRepository.findByUsername(principal.getName());

	 

	        String id = loggedU.getUsername();
	        MasterEmployeeDetails updatedBy = this.masterEmployeeDetailsRepository.findByLancesoft(id);

	 

	        MasterEmployeeDetails masterEmployeeDetails = masterEmployeeDetailsRepository.findById(relaseEmp.getEmpId())
	                .get();

	 

	        Optional<ReleaseEmpDetails> rel = releaseEmpDetailsRepository
	                .findBymasterEmployeeDetails_Id(relaseEmp.getEmpId());
	        
	        System.err.println(relaseEmp.getEmpId());
	        
	        
	        ReleaseEmpDetails releaseEmpDetails = new ReleaseEmpDetails();
	        
	        
	        
 
	        
	        
	        
	        if (masterEmployeeDetails == null) {
	            throw new EmptyInputException("given employee id is not present");
	        }
	       
	        
	  else 
	  {
	            if (rel.isPresent()) {
	 
System.out.println(rel+"===============================");
	                throw new RecordAlredyExistException("record already released", "77");
	         }
	            
	            
	            
	 else {

	 

	             
	                releaseEmpDetails.setMasterEmployeeDetailsId(relaseEmp);
	                releaseEmpDetails.setMasterEmployeeDetailssupervisor(relaseEmp.getSupervisor());
	                releaseEmpDetails.setReleaseStatus(ReleaseStatus.APPROVED);
	                releaseEmpDetails.setReleaseDate(releaseDate);
	                releaseEmpDetailsRepository.save(releaseEmpDetails);

	 

	              
	 

	                History historyEmp = new History(masterEmployeeDetails.getLancesoft(),
	                        masterEmployeeDetails.getFirstName(), masterEmployeeDetails.getLastName(),
	                        masterEmployeeDetails.getJoiningDate(), masterEmployeeDetails.getDOB(),
	                        masterEmployeeDetails.getLocation(), masterEmployeeDetails.getGender(),
	                        masterEmployeeDetails.getEmail(), masterEmployeeDetails.getCreatedAt(),
	                        masterEmployeeDetails.getVertical(), masterEmployeeDetails.getStatus(),
	                        masterEmployeeDetails.getAge(), masterEmployeeDetails.getIsInternal(),
	                        masterEmployeeDetails.getPhoneNo(), masterEmployeeDetails.getCreatedBy(),
	                        masterEmployeeDetails.getSubDepartments(), masterEmployeeDetails.getDepartments(),
	                        masterEmployeeDetails.getDesignations(), masterEmployeeDetails.getSupervisor(),
	                        masterEmployeeDetails.getEmployeeType(), UpdatedStatus.RELEASED, LocalDate.now(), updatedBy);
	                historyOfEmpRepository.save(historyEmp);

	 

	                masterEmployeeDetails.setStatus(EmployeeStatus.EXIT);

	 

	                masterEmployeeDetailsRepository.save(masterEmployeeDetails);

	 

	            }
	  }
	        

	 

	            User u = userRepository.findByUsername(relaseEmp.getLancesoft());

	 

	            if (u != null) {
	                userRepository.delete(u);

	 

	            }
	            return masterEmployeeDetails;
	        }
	    }

	 

	

  