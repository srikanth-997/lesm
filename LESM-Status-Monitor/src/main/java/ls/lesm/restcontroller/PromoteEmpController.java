package ls.lesm.restcontroller;



import java.security.Principal;
import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.AtClientAllowances;
import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.repository.AllowancesOfEmployeeRepo;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.service.impl.PromoteEmpServiceImp;

@Component
@RestController
@CrossOrigin("*")
public class PromoteEmpController {

	@Autowired
	PromoteEmpServiceImp promoteEmpServiceImp;
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	

	@Autowired
	AllowancesOfEmployeeRepo allowancesOfEmployeeRepo;

	@GetMapping("/PromoteEmpDetails/{Emp_Id}/{Super_Id1}/{Super_Id2}/{salary}")
    public ResponseEntity<String> promoteEmp(@PathVariable("Emp_Id") String emp, @PathVariable("Super_Id1")String superId, @PathVariable("Super_Id2") String secmanager

            ,@PathVariable double  salary,Principal principal) {

 

        promoteEmpServiceImp.promoteEmployeeDetailss(emp,superId,secmanager, salary,principal );

 

        return new ResponseEntity<String>("Promoted successfully", HttpStatus.CREATED);

 

    }



    @GetMapping("/ReportsToPrimary/{lanceId}")
    List<MasterEmployeeDetails> primaryReporting(@PathVariable String lanceId)
    {

    return promoteEmpServiceImp.primary(lanceId);

 

          
    }


    @GetMapping("/ReportsToSecondary/{lanceId}/{primaryId}")
    List<MasterEmployeeDetails> secondReporting(@PathVariable String lanceId, @PathVariable("primaryId") String Sub_id1)
    {


        return promoteEmpServiceImp.secondaryreportstoDashboard(lanceId,Sub_id1);

 

          
    }
//    @GetMapping("/Submit_the_allowances/{shiftAllowances}/{specialAllowances}/{joiningBonus}/{joiningBonusTenure}/{EmployeeId}")
//	public ResponseEntity<?>  enterAllowances(@PathVariable Double shiftAllowances, @PathVariable Double specialAllowances,
//			@PathVariable Double joiningBonus,@PathVariable Integer joiningBonusTenure,
//			@PathVariable String EmployeeId) {
//
//		MasterEmployeeDetails employee = masterEmployeeDetailsRepository.findByLancesoft(EmployeeId);
//		if (employee == null) {
//
//			return new ResponseEntity<>("Invalid Employee Id",HttpStatus.OK);
//			
//			
//			
//
//		}
//
//		else {
//
//			if (shiftAllowances == null)
//				shiftAllowances = 0.0;
//
//			if (specialAllowances == null)
//				specialAllowances = 0.0;
//
//			if (joiningBonus == null)
//				joiningBonus = 0.0;
//
//			if (joiningBonusTenure == null)
//				joiningBonusTenure = 0;
//
//			allowancesOfEmployeeRepo.save(new AtClientAllowances(shiftAllowances, specialAllowances, joiningBonus,
//					joiningBonusTenure, employee));
//
//			return new ResponseEntity<>("Data inserted",HttpStatus.CREATED);
//		}
//	}
}


