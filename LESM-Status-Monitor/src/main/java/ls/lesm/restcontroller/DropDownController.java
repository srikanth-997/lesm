package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.Clients;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.enums.ResponsibilitiesTypes;
import ls.lesm.payload.request.UpdateClientDetialsDropDown;
import ls.lesm.payload.response.ConsultantDropDownRes;
import ls.lesm.payload.response.DesignatinsResponse;
import ls.lesm.payload.response.EmployeesDropDownResponse;
import ls.lesm.payload.response.ReportToDropDownRes;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.service.impl.DeleteEmployeeService;
import ls.lesm.service.impl.DropDownServiceImpl;
import ls.lesm.service.impl.RecruiterServiceImpl;

@RestController
@RequestMapping("/api/v1/drop-down")
@CrossOrigin("*")
public class DropDownController {

	@Autowired
	private RecruiterServiceImpl recruiterServiceImpl;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	private ClientsRepository clientRepo;

	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@Autowired
	private DropDownServiceImpl dropDownService;

	@Autowired
	private DesignationsRepository designationsRepository;


	@Autowired
	DeleteEmployeeService deleteEmp;

	@GetMapping("/rec")
	public ResponseEntity<List<EmployeesDropDownResponse>> temp(Principal principal) {

		return new ResponseEntity<List<EmployeesDropDownResponse>>(
				this.recruiterServiceImpl.recruitersDropDown(principal), HttpStatus.OK);
	}

	@GetMapping("/employee-clients")
	public ResponseEntity<List<UpdateClientDetialsDropDown>> empClientDropDown(@RequestParam int empId) {

		Optional<MasterEmployeeDetails> emp = this.masterEmployeeDetailsRepository.findById(empId);
		List<EmployeesAtClientsDetails> clientList = this.employeesAtClientsDetailsRepository
				.findByMasterEmployeeDetails(emp.get());

		List<UpdateClientDetialsDropDown> list = new ArrayList<UpdateClientDetialsDropDown>();

		for (EmployeesAtClientsDetails c : clientList) {
			UpdateClientDetialsDropDown dropD = new UpdateClientDetialsDropDown();
			dropD.setClientId(c.getEmpAtClientId());
			dropD.setClientName(c.getClients().getClientsNames());
			dropD.setPoEDate(c.getPOEdate());
			dropD.setPoSDate(c.getPOSdate());
			list.add(dropD);
		}

		return new ResponseEntity<List<UpdateClientDetialsDropDown>>(list, HttpStatus.OK);

	}

	@GetMapping("/clients-name")
	public ResponseEntity<List<Clients>> getClientNameByNameKey(@RequestParam String name) {

		List<Clients> clients = this.clientRepo.findByClientsNamesStartsWith(name);

		return new ResponseEntity<List<Clients>>(clients, HttpStatus.OK);
	}

	@GetMapping("/consultants")
	public ResponseEntity<List<ConsultantDropDownRes>> getAllCons(@RequestParam String lancesoftId,
			@RequestParam int subDId) {

		return new ResponseEntity<List<ConsultantDropDownRes>>(
				this.dropDownService.getAllConsByPractice(subDId, lancesoftId), HttpStatus.OK);
	}

	@GetMapping("/employee-by-desig")
	public ResponseEntity<List<ReportToDropDownRes>> getByDesigAndSearch(@RequestParam String keyword,
			@RequestParam int desgId) {

		return new ResponseEntity<List<ReportToDropDownRes>>(
				this.dropDownService.getEmployeeByDesigAndSearch(desgId, keyword), HttpStatus.OK);
	}

	@GetMapping("/report-to-desigs")
	public ResponseEntity<List<Designations>> getEqualandAboveDesig(@RequestParam int desigId) {

		return new ResponseEntity<List<Designations>>(this.dropDownService.getAllDesignationsEqualOrAbove(desigId),
				HttpStatus.OK);
	}

	@GetMapping("/primary-manager-desig")
	public ResponseEntity<List<Designations>> getAboveDesigs(@RequestParam int desigId) {

		return new ResponseEntity<List<Designations>>(this.dropDownService.getAboveAllDesignations(desigId),
				HttpStatus.OK);
	}

	@GetMapping("/designaton")
	public ResponseEntity<List<DesignatinsResponse>> getDesignationExceptHr() {

		return new ResponseEntity<List<DesignatinsResponse>>(this.dropDownService.getAllDesignationExceptHR(),
				HttpStatus.OK);
	}

	

	// this's temp api, later need to replace with "/employee-by-desig";
	@GetMapping("/all-employee-by-desig")
	public ResponseEntity<List<ReportToDropDownRes>> getEmpByDesig(@RequestParam int desgId) {
		ArrayList<EmployeeStatus> statusList = new ArrayList<EmployeeStatus>();
		statusList.add(EmployeeStatus.BENCH);
		statusList.add(EmployeeStatus.CLIENT);
		statusList.add(EmployeeStatus.MANAGMENT);

		List<MasterEmployeeDetails> employees = this.masterEmployeeDetailsRepository
				.findAllByDesignationsAndStatusIn(this.designationsRepository.findById(desgId).get(), statusList);
		List<ReportToDropDownRes> list = new ArrayList<ReportToDropDownRes>();
		for (MasterEmployeeDetails m : employees) {
			// if(m.getStatus().equals(EmployeeStatus.CLIENT)||m.getStatus().equals(EmployeeStatus.BENCH)||m.getStatus().equals(EmployeeStatus.MANAGMENT))
			// {
			ReportToDropDownRes res = new ReportToDropDownRes();
			res.setDesigId(m.getDesignations().getDesgId());
			res.setEmpId(m.getEmpId());
			res.setLancesoftId(m.getLancesoft());
			res.setName(StringUtils.capitalize(m.getFirstName().toLowerCase()) + " "
					+ StringUtils.capitalize(m.getLastName().toLowerCase()));
			list.add(res);
			// }
		}
		return new ResponseEntity<List<ReportToDropDownRes>>(list, HttpStatus.OK);
	}

	@GetMapping("/responsibilites")
	public ResponseEntity<List<ResponsibilitiesTypes>> responsiblityDropDown(@RequestParam Integer empId){
		
		return new ResponseEntity<List<ResponsibilitiesTypes>>(this.dropDownService.responsibilityDropDown(empId),HttpStatus.OK);
	}
	
	@GetMapping("/employees-by-responsibilites")
	public ResponseEntity<Set<EmployeesDropDownResponse>> getEmployeesByResposibility(@RequestParam String keyword,@RequestParam ResponsibilitiesTypes types){
		
		return new ResponseEntity<Set<EmployeesDropDownResponse>>(this.dropDownService.getEmployeesDropDownByResponsibilityWithSearch(keyword,types),HttpStatus.OK);
	}
	
	
}
