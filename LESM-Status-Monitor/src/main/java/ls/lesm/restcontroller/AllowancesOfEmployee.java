package ls.lesm.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.AtClientAllowances;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.AllowancesOfEmployeeRepo;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;

@CrossOrigin("*")
@RestController
public class AllowancesOfEmployee {

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	AllowancesOfEmployeeRepo allowancesOfEmployeeRepo;

	@Autowired
	private EmployeesAtClientsDetailsRepository clientDetail;

	@GetMapping("/SubmitTheAllowances/{extraAllowances}/{depAllowances}/{shiftAllowances}/{specialAllowances}/{joiningBonus}/{joiningBonusTenure}/{EmployeeId}")
	public ResponseEntity<?> enterAllowances(@PathVariable Double extraAllowances, @PathVariable Double depAllowances,
			@PathVariable Double shiftAllowances, @PathVariable Double specialAllowances,
			@PathVariable Double joiningBonus, @PathVariable Integer joiningBonusTenure,
			@PathVariable String EmployeeId) {

		MasterEmployeeDetails employee = masterEmployeeDetailsRepository.findByLancesoft(EmployeeId);
		if (employee == null) {

			return new ResponseEntity<>("Invalid Employee Id", HttpStatus.OK);

		}

		else {

			if (depAllowances == null) {
				depAllowances = 0.0;
			}
			if (extraAllowances == null) {
				extraAllowances = 0.0;
			}
			if (shiftAllowances == null)
				shiftAllowances = 0.0;

			if (specialAllowances == null)
				specialAllowances = 0.0;

			if (joiningBonus == null)
				joiningBonus = 0.0;

			if (joiningBonusTenure == null)
				joiningBonusTenure = 0;

			allowancesOfEmployeeRepo.save(new AtClientAllowances(shiftAllowances, specialAllowances, joiningBonus,
					joiningBonusTenure, extraAllowances, depAllowances, employee));

			return new ResponseEntity<>("Data inserted", HttpStatus.OK);
		}
	}

	@GetMapping("/getAllowances")
	public ResponseEntity<List<AtClientAllowances>> enterAllowances(@RequestParam int employeeId) {
		MasterEmployeeDetails employee = masterEmployeeDetailsRepository.findById(employeeId).get();

		List<AtClientAllowances> atallowances = allowancesOfEmployeeRepo
				.findsBymasterEmployeeDetails_Id(employee.getEmpId());
		if (!atallowances.isEmpty()) {
			return new ResponseEntity<List<AtClientAllowances>>(atallowances, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

	@PostMapping("/getEmployeesForAllowances")
	public ResponseEntity<List<MasterEmployeeDetails>> getEmployeesForAllowancesDropdown()

	{

//		System.out.println("1");
		List<MasterEmployeeDetails> employeesForAllowances = new ArrayList<MasterEmployeeDetails>();

		List<EmployeesAtClientsDetails> employeesAtClient = clientDetail.findAll();

		System.out.println(employeesAtClient);

		for (EmployeesAtClientsDetails employee : employeesAtClient)

		{

			employeesForAllowances.add(employee.getMasterEmployeeDetails());

		}

		if (!employeesAtClient.isEmpty()) {

			return new ResponseEntity<List<MasterEmployeeDetails>>(employeesForAllowances, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		}

	}

}
