package ls.lesm.restcontroller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;

@RestController
@CrossOrigin("*")
public class SalaryUpdate {

	@Autowired
	SalaryRepository salaryRepository;

	@Autowired
	MasterEmployeeDetailsRepository employeeDetailsRepository;

//	@PostMapping("/UpdateSalary")
//	public ResponseEntity<?> updateSalary(@RequestBody SalaryHikeUpdate salaryupdate) {
//
//		if (salaryupdate.getUpdatedAt() == null) {
//			salaryupdate.setUpdatedAt(LocalDate.now());
//		}
//		//sudheer
//		
//		System.err.println("sudheer");
//
//		MasterEmployeeDetails employee = employeeDetailsRepository.findByLancesoft(salaryupdate.getEmpId());
//		try {
//
//			salaryRepository.save(new Salary(salaryupdate.getSalary(), employee.getCreatedAt(), salaryupdate.getUpdatedAt(), employee.getCreatedBy(), employee));
//
//			return new ResponseEntity<String>("updated salary  successfully", HttpStatus.OK);
//		}
//
//		catch (Exception e) {
//			return new ResponseEntity<String>("Salary Error" + e, HttpStatus.BAD_REQUEST);
//
//		}
//
//	}

	@GetMapping("/UpdateSalary/{empId}/{salary}/{updateDate}")
	public ResponseEntity<?> updateSalary(@PathVariable String empId, @PathVariable double salary,
			@PathVariable("updateDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate updateDate) {
		
		//LocalDate updateDate2=LocalDate.parse(updateDate);

		if (updateDate == null) {
			updateDate = LocalDate.now();

		}

		MasterEmployeeDetails employee = employeeDetailsRepository.findByLancesoft(empId);
		try {

			salaryRepository
					.save(new Salary(salary, employee.getCreatedAt(), updateDate, employee.getCreatedBy(), employee));

			return new ResponseEntity<String>("updated salary  successfully", HttpStatus.OK);
		}

		catch (Exception e) {
			return new ResponseEntity<String>("Salary Error" + e, HttpStatus.BAD_REQUEST);

		}

	}

}
