package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.model.InternalExpenses;
import ls.lesm.payload.request.EmployeeAtClientRequest;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.AddressTypeRepository;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeePhotoRepo;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SecondaryManagerRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.IImageService;
import ls.lesm.service.impl.ClientServiceImpl;
import ls.lesm.service.impl.EmployeeDetailsServiceImpl;

@RestController
@RequestMapping("/api/v1/client")
@CrossOrigin("*")
public class ClientController {



	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	private DepartmentsRepository departmentsRepository;
	@Autowired
	private SubDepartmentsRepository subDepartmentsRepositorye;
	@Autowired
	private DesignationsRepository designationsRepository;
	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;

	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@Autowired
	private ClientsRepository clientsRepository;

	@Autowired
	private AddressRepositoy addressRepositoy;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressTypeRepository addressTypeRepository;

	@Autowired
	private IImageService imageService;

	@Autowired
	private EmployeePhotoRepo employeePhotoRepo;

	@Autowired
	private SalaryRepository salaryRepo;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private SecondaryManagerRepository secondaryManagerRepository;

	@Autowired
	private InternalExpensesRepository internalExpensesRepository;

	@Autowired
	private ClientServiceImpl clientService;

	// UMER
	@PostMapping("/details")
	public ResponseEntity<?> insertEmpAtClient(@RequestBody EmployeeAtClientRequest clientDetails,
			Principal principal) {

		this.clientService.addClientDetailsToEmployee(clientDetails, principal);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@PutMapping("/edit")
	public ResponseEntity<?> editEmpAtClient(@RequestBody EmployeeAtClientRequest clientDetails,
			@RequestParam Integer clientDetailId, Principal principal) {

		this.clientService.editClientDetailsForEmployee(clientDetailId, clientDetails, principal);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/edit-request")
	public ResponseEntity<EmployeeAtClientRequest> editEmpAtClientRequest(@RequestParam Integer clientDetailId) {

		return new ResponseEntity<EmployeeAtClientRequest>(this.clientService.editClientDetailRequest(clientDetailId),
				HttpStatus.OK);
	}

	@GetMapping("/get_enhancedFields/{empId}")
	public ResponseEntity<Map<String, InternalExpenses>> getFields(@PathVariable int empId) {

		Map<String, InternalExpenses> m = new HashMap<String, InternalExpenses>();

		InternalExpenses internalExpenses = new InternalExpenses();

		Optional<InternalExpenses> details = internalExpensesRepository.findBymasterEmployeeDetails_Id(empId);

		if (details.isPresent()) {

			m.put("enhancedFields", details.get());
		} else {
			internalExpenses.setBenchTenure(0);
			internalExpenses.setBR_INR(0.0);
			internalExpenses.setBR_USD(0.0);
			internalExpenses.setCreatedAt(null);
			internalExpenses.setCreatedBy(null);
			internalExpenses.setGM(0.0);
			internalExpenses.setGPM_INR(0.0);
			internalExpenses.setGPM_INR(0.0);
			internalExpenses.setGPM_USD(0.0);
			internalExpenses.setInternalExpId(0);
			internalExpenses.setMasterEmployeeDetails(null);
			internalExpenses.setPR_INR(0.0);
			internalExpenses.setPR_USD(0.0);
			internalExpenses.setProfitOrLoss(0.0);
			internalExpenses.setTotalExpenses(0.0);
			internalExpenses.setTotalSalPaidTillNow(0.0);

			m.put("enhancedFields", internalExpenses);

		}
		return new ResponseEntity<Map<String, InternalExpenses>>(m, HttpStatus.OK);

	}

}
