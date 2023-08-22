package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.impl.TransferEmployeeService;

@CrossOrigin("*")
@RestController
public class TransferEmployee {

	@Autowired
	MasterEmployeeDetailsRepository employeeDetailsRepository;
	@Autowired
	UserRepository userRepo;
	@Autowired
	TransferEmployeeService transferEmployeeService;

	
	@GetMapping("/getAllDesignation")
	public ResponseEntity<List<Designations>> getALLDessignation() {

		List<Designations> Designations = transferEmployeeService.GetAllDesignation();

		List<Designations> filter = new ArrayList<Designations>();

		Pattern p = Pattern.compile("HR", Pattern.CASE_INSENSITIVE);
		Pattern p1 = Pattern.compile("Managing Director", Pattern.CASE_INSENSITIVE);
		Pattern p2 = Pattern.compile("MD", Pattern.CASE_INSENSITIVE);

		for (Designations de : Designations) {

			Matcher m = p.matcher(de.getDesgNames());
			Matcher m1 = p1.matcher(de.getDesgNames());
			Matcher m2 = p2.matcher(de.getDesgNames());

			if (m.matches() || m1.matches() || m2.matches()) {
				continue;

			}
			filter.add(de);

		}

		return new ResponseEntity<List<Designations>>(filter, HttpStatus.OK); 
	}

	@GetMapping("/getByDesignation")
	public ResponseEntity<List<MasterEmployeeDetails>> getEmployeesByDesignation(@RequestParam int des_id) {

		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.GetEmployees(des_id);

		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);

	}

	@GetMapping("/newsupervisiorsinHRDashboard")
	public ResponseEntity<List<MasterEmployeeDetails>> newsupervisiorsinHRDashboard(@RequestParam String LancesoftId) {

		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.getnewSupervisiorsinHrDashboard(LancesoftId);

		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);

	}

	@GetMapping("/reportstodropdownsuperadmin")
	public ResponseEntity<List<MasterEmployeeDetails>> reportsto(@RequestParam String LancesoftId) {

		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.reportstoDashboard(LancesoftId);

		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);
	}

	@GetMapping("/secondaryreportstodropdownsuperadmin")
	public ResponseEntity<List<MasterEmployeeDetails>> secondaryreportsto(@RequestParam String LancesoftId,@RequestParam String primaryLancesoftId) {

		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.secondaryreportstoDashboard(LancesoftId,primaryLancesoftId);
      
		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);
	}

	@GetMapping("/transfer")
	public ResponseEntity<?> transferEmployee(@RequestParam String LancesoftId, @RequestParam String primaryLancesoftId,
			@RequestParam String secondaryLancesoftId, @RequestParam String Location, @RequestParam Double Salary,
			Principal principal) {
		
		System.out.println("C");

		transferEmployeeService.transferEmployeeToSameDesignation(LancesoftId, primaryLancesoftId, secondaryLancesoftId,
				Location, Salary, principal);
		return new ResponseEntity<>("transfer and update successfully", HttpStatus.OK);

	}
	
	
	
	
	
	
	
	
	
	
	
//	@GetMapping("/getAllEmployeeUndercurrentlogin")
//	public ResponseEntity<List<MasterEmployeeDetails>> AllEmployeesUnderCL(Principal principal) {
//
//		List<MasterEmployeeDetails> K = transferEmployeeService.getAllEmployeeUnderCL(principal);
//
//		return new ResponseEntity<List<MasterEmployeeDetails>>(K, HttpStatus.OK);
//
//	}

//	@GetMapping("/AssignToEmployee")
//	public ResponseEntity<?> getAllSameDesignations(@RequestParam String LancesoftId) {
//
//		List<MasterEmployeeDetails> assignToemployee = transferEmployeeService.transferService(LancesoftId);
//
//		if (assignToemployee.isEmpty())
//			throw new UserNameNotFoundException("Employees with this designationss are not there");
//
//		if (assignToemployee.isEmpty())
//			throw new UserNameNotFoundException(" NO Employess with this designations ");
//		else
//
//			return new ResponseEntity<>(assignToemployee, HttpStatus.OK);
//
//	}
//
//	@PostMapping("/saveAndNext")
//	public ResponseEntity<?> getAllSameDesignations(@RequestParam String LancesoftId, @RequestParam String assign_id) {
//
//		transferEmployeeService.saveAndnext(LancesoftId, assign_id);
//		return new ResponseEntity<>("updated supervisior", HttpStatus.OK);
//
//	}

//	@GetMapping("/principalDesignationsDropdown")
//	public ResponseEntity<List<MasterEmployeeDetails>> getAllSameDesignations(Principal principal) {
//
//		List<MasterEmployeeDetails> MasterEmployeeDetails = transferEmployeeService.newsupervisiorIDs(principal);
//
//		return new ResponseEntity<List<MasterEmployeeDetails>>(MasterEmployeeDetails, HttpStatus.OK);
//
//	}
//
//	@GetMapping("/transfer")
//	public ResponseEntity<?> transferEmployee(@RequestParam String LancesoftId, @RequestParam String TansferLancesoftId,
//			@RequestParam String Location,@RequestParam Double Salary, Principal principal) {
//
//		transferEmployeeService.transferEmployeeToSameDesignation(LancesoftId, TansferLancesoftId, Location,Salary,
//				principal);
//		return new ResponseEntity<>("transfer and update successfully", HttpStatus.OK);
//
//	}

	/////////////////////

//	@GetMapping("/getAllSameDesignationsMR")
//	public ResponseEntity<?> getAllSameDesignationsMR(@RequestParam String LancesoftId, Principal principal) {
//		transferEmployeeService.transferServiceMR(LancesoftId, principal);
//
//		return new ResponseEntity<>("Tr", HttpStatus.OK);
//	}

	// updated Add-ons hr dashborad

	

	//// Manager Dashboard

//	@GetMapping("/consultants")
//	public ResponseEntity<List<MasterEmployeeDetails>> consultantsinManagerDashboard(Principal principal) {
//
//		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.consultantsinManagerDashboard(principal);
//
//		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);
//
//	}
//
//	@GetMapping("/reportstodropdown")
//	public ResponseEntity<List<MasterEmployeeDetails>> reportstoinManagerDashboard(String LancesoftId) {
//		List<MasterEmployeeDetails> reportstoDropdown = transferEmployeeService.reportstoDropdown(LancesoftId);
//		return new ResponseEntity<List<MasterEmployeeDetails>>(reportstoDropdown, HttpStatus.OK);
//	}

//	@GetMapping("/Leadsdropdown")
//	public ResponseEntity<List<MasterEmployeeDetails>> leadsinManagerDashboard(Principal principal) {
//
//		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.leadsinManagerDashboard(principal);
//
//		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);
//
//	}

//    @PutMapping("/updateLocation")
//    public ResponseEntity<?> updateWorkLocation(@RequestParam int employee_id, @RequestParam String newLocation,
//            @RequestParam int newsupervisor,Double newsalary) {
//
//        transferEmployeeService.updateLocation(employee_id, newLocation, newsupervisor,newsalary);
//
//        return new ResponseEntity<>("updated Succesfully", HttpStatus.OK);
//    }

//	@PutMapping("/updateLocation")
//	public ResponseEntity<?> updateWorkLocation(@RequestBody TransferRequest transferRequest, Principal principal) {
//
//		String emp1 = principal.getName();
//		MasterEmployeeDetails updatedBy = this.employeeDetailsRepository.findByLancesoft(emp1);
//
//		transferEmployeeService.updateLocation(transferRequest.masterEmployeeDetails.getEmpId(),
//				transferRequest.masterEmployeeDetails.getLocation(),
//				transferRequest.masterEmployeeDetails.getSupervisor().getEmpId(), transferRequest.salary.getSalary(),
//				updatedBy);
//
//		return new ResponseEntity<>("updated Succesfully", HttpStatus.OK);
//	}
//
//	@GetMapping("/getByDesignationandsub_department")
//	public ResponseEntity<List<MasterEmployeeDetails>> getEmployeesByDessignationandDepartment(
//			@RequestParam int des_id) {
//
//		List<MasterEmployeeDetails> employeeDetails = transferEmployeeService.GetEmployeesByDesignationandSubD(des_id);
//
//		return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, HttpStatus.OK);
//
//	}
//
//	@PutMapping("/getin")
//
//	public ResponseEntity<?> getInfo(@RequestParam int updatedSupervisio_id, @RequestParam("sup_id") int sup_id) {
//
//		transferEmployeeService.updateEmployeeDetailsUpdate(updatedSupervisio_id, sup_id);
//
//		return new ResponseEntity<>("updated supervisor", HttpStatus.OK);
//	}

//    @PutMapping("/updateDetails/{employee_id}/{newLocation}/{newsalary}/{newSubDep}/{newsupervisor}")
//    public ResponseEntity<?> updateDetails(@PathVariable int employee_id, @PathVariable String newLocation,
//            @PathVariable Double newsalary, @PathVariable int newSubDep, @PathVariable int newsupervisor) {
//
//        transferEmployeeService.updateDetails(employee_id, newLocation, newsalary, newSubDep, newsupervisor);
//
//        return new ResponseEntity<>("updated Details  Succesfully", HttpStatus.OK);
//    }

//	@PutMapping("/updateDetails")
//	public ResponseEntity<?> updateDetails(@RequestBody TransferRequest transferRequest, Principal principal) {
//
//		String emp1 = principal.getName();
//		MasterEmployeeDetails updatedBy = this.employeeDetailsRepository.findByLancesoft(emp1);
//
//		transferEmployeeService.updateDetails(transferRequest.masterEmployeeDetails.getEmpId(),
//				transferRequest.masterEmployeeDetails.getLocation(), transferRequest.salary.getSalary(),
//				transferRequest.subDepartments.getSubDepartId(),
//				transferRequest.masterEmployeeDetails.getSupervisor().getEmpId(), updatedBy);
//
//		return new ResponseEntity<>("updated Details  Succesfully", HttpStatus.OK);
//	}
	
	
	 @GetMapping("/user-device")
	    public String getUserDevice(HttpServletRequest request) {
	        String userAgent = request.getHeader("User-Agent");
	        return "User device: " + userAgent;
	    }
	 
	 
	    
	 @GetMapping("/device")
	    public String getDevice(Device device) {
	        if (device.isMobile()) {
	            return "User login device is a mobile device";
	        } else if (device.isTablet()) {
	            return "User login device is a tablet device";
	        } else {
	            return "User login device is a desktop/laptop computer";
	        }
	 }

}
