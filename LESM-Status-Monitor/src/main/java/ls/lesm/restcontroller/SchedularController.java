package ls.lesm.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.service.impl.EmployeeDetailsServiceImpl;

@RestController

@CrossOrigin("*")
public class SchedularController {

	@Autowired
	private EmployeeDetailsServiceImpl employeeDetailsService;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@GetMapping("/settingStatus")
	public ResponseEntity<?> allEmpDetailsAtClient() {

		// LocalDate l=null;

		List<MasterEmployeeDetails> all = this.masterEmployeeDetailsRepository.findAll();

		for (MasterEmployeeDetails e : all) {

			List<EmployeesAtClientsDetails> allClients = employeesAtClientsDetailsRepository
					.findsBymasterEmployeeDetails_Id(e.getEmpId());// u

			if (!allClients.isEmpty())

			{

				for (EmployeesAtClientsDetails clients : allClients) {

					if (clients.getPOEdate() == null || clients.getPOEdate().equals(LocalDate.now())
							|| clients.getPOEdate().isAfter(LocalDate.now())) {
						e.setStatus(EmployeeStatus.CLIENT);
						masterEmployeeDetailsRepository.save(e);
						break;

					}

					else {
						e.setStatus(EmployeeStatus.BENCH);
						masterEmployeeDetailsRepository.save(e);
					}

				}
			}
		}
		return new ResponseEntity(" Schedule suceesfully", org.springframework.http.HttpStatus.OK);

	}
}
