package ls.lesm.restcontroller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.impl.DemoteEmpService;

@RestController
@CrossOrigin("*")
public class DemoteEmpController {
	@Autowired
	DesignationsRepository designationsRepository;
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	HistoryRepository historyRepository;

	@Autowired
	DemoteEmpService demoteEmpService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	SalaryRepository salaryRepository;

	@GetMapping("/getAllDemoteDesignations")
	List<Designations> getEmployees() {

		return demoteEmpService.getEmp();
	}

	@GetMapping("/chooseDesignstionToAssign/{des_id}")

	List<Designations> getDesigantionsToAssign(@PathVariable int des_id) {

		Optional<Designations> designationss = designationsRepository.findById(des_id);

		return demoteEmpService.chooseDesignstionToAssign(designationss.get());

	}

	@PostMapping("/getAlldesignationEmployees/{id}")

	List<MasterEmployeeDetails> getAllDesignateEmployees(@PathVariable int id) {

		return demoteEmpService.getAllDesignateEmployes(id);

	}

	@GetMapping("/getPrimarySupervisor/{des_id}/{empId}")

	List<?> addSupervisor(@PathVariable int des_id,@PathVariable String empId) {

		Optional<Designations> designationss = designationsRepository.findById(des_id);

		
		return demoteEmpService.addSupervisor1lan(designationss.get().getDesgId(),empId);

	}

	@GetMapping("/getSecondarySupervisor")

	List<MasterEmployeeDetails> addSecondSupervisor(@RequestParam(required = false, defaultValue = "null") int des_id,

			@RequestParam(required = false, defaultValue = "null") String superId,String empId) {

		Optional<Designations> designationss = designationsRepository.findById(des_id);

		return demoteEmpService.addSecondSupervisor1(designationss.get().getDesgId(), superId,empId);

	}

	@GetMapping("/submit/{emp_Id}/{super_Id1}/{super_Id2}/{Salary}/{updated_date}/{newDesg}")

	ResponseEntity<String> demote(@PathVariable("emp_Id") String emp,

			@PathVariable("super_Id1") String superId1, @PathVariable("super_Id2") String superId2,

			@PathVariable("Salary") double salary, Principal principal, @PathVariable String updated_date,

			@PathVariable Designations newDesg)

	{

		System.out.println(newDesg+"new desg");
		
		return demoteEmpService.demote1(emp, superId1, superId2, salary, principal, LocalDate.parse(updated_date),
				newDesg);

	}

}
