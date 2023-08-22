package ls.lesm.restcontroller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.exp.Accommodation;
import ls.lesm.model.exp.Allowance;
import ls.lesm.model.exp.ExpenseNotification;
import ls.lesm.model.exp.Food;
import ls.lesm.model.exp.OtherExpenses;
import ls.lesm.model.exp.Travel;
import ls.lesm.payload.request.ApprovalStatusRequest;
import ls.lesm.payload.request.ExpensesRequest;
import ls.lesm.payload.response.AllExpensesResponse;
import ls.lesm.payload.response.InitialExpenseDetailResponse;
import ls.lesm.payload.response.NotificationResponse;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.expRepo.AccomodationRepo;
import ls.lesm.repository.expRepo.AllowanceRepo;
import ls.lesm.repository.expRepo.ExpenseNotificatonRepo;
import ls.lesm.repository.expRepo.FoodRepo;
import ls.lesm.repository.expRepo.OtherExpensesRepo;
import ls.lesm.repository.expRepo.TravelRepo;
import ls.lesm.service.impl.ExpenseServiceImpl;

@RestController
@RequestMapping("api/v1/exp")
@CrossOrigin("*")
public class ExpensesController {

	@Autowired
	private ExpenseServiceImpl expenseService;

	@Autowired
	private ExpenseNotificatonRepo expenseNotificatonRepo;

	@Autowired
	private FoodRepo foodRepo;

	@Autowired
	private AllowanceRepo allowanceRepo;

	@Autowired
	private TravelRepo travelRepo;

	@Autowired
	private AccomodationRepo accomodationRepo;

	@Autowired
	private OtherExpensesRepo otherExpensesRepo;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	// UMER
	@PostMapping("/insert-sal")
	public ResponseEntity<?> insertSal(@RequestParam Integer empId, @RequestBody Salary sal, Principal principal) {
		this.expenseService.inserSal(sal, principal, empId);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	// UMER
	@PostMapping("/insert-expenses")
	public ResponseEntity<?> insertExpenses(Principal principal, @RequestBody ExpensesRequest expensesResponse) {
		this.expenseService.insertExpenses(principal, expensesResponse);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-food-exp-manager")
	public ResponseEntity<?> approveFoodExpByManager(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveFoodExpByManager(principal, req);
		return new ResponseEntity<>(req, HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-food-exp-finance")
	public ResponseEntity<?> approveFoodExpByFinance(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveFoodExpByFinance(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-travel-exp-manager")
	public ResponseEntity<?> approveTravelExpByManager(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveTravelExpByManager(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-travel-exp-finance")
	public ResponseEntity<?> approveTravelExpByFinance(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveTravelExpByFinance(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-accmd-exp-manager")
	public ResponseEntity<?> approveAccomodationExpByManager(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveAccomodationExpByManager(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-accmd-exp-finance")
	public ResponseEntity<?> approveAccomodationExpByFinance(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveAccomodationExpByFinance(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-allowance-exp-manager")
	public ResponseEntity<?> approveAllowanceExpByManager(@RequestBody ApprovalStatusRequest req, Principal principal) {
		this.expenseService.approveAllowanceExpByManager(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-allowance-exp-finance")
	public ResponseEntity<?> approveAllowanceExpByFinance(@RequestBody ApprovalStatusRequest req, Principal principal) {
		this.expenseService.approveAllowanceExpByFinance(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-other-exp-manager")
	public ResponseEntity<?> approveOtherExpByManager(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveOtherExpByManager(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/approve-other-exp-finance")
	public ResponseEntity<?> approveOtherExpByFinance(@RequestBody List<ApprovalStatusRequest> req,
			Principal principal) {
		this.expenseService.approveOtherExpByFinance(principal, req);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// UMER
	// supervisor or emp
	@GetMapping("/exp-card-details")
	public ResponseEntity<List<InitialExpenseDetailResponse>> getCardDetails(Principal principal) {
		List<InitialExpenseDetailResponse> data = this.expenseService.getCardDetails(principal);
		return new ResponseEntity<List<InitialExpenseDetailResponse>>(data, HttpStatus.OK);
	}

	// UMER
	@GetMapping("/exp-full-details")
	public ResponseEntity<AllExpensesResponse> get(@RequestParam Integer totalId) {

		AllExpensesResponse data = this.expenseService.getAllExpByTotalId(totalId);
		return new ResponseEntity<AllExpensesResponse>(data, HttpStatus.OK);
	}

	// UMER
	@PostMapping("/food-exp-bill")
	public ResponseEntity<Map<String, String>> uploadFoodExpFile(@RequestParam Integer foodId,
			@RequestParam("file") MultipartFile file) {
		String publicURL = expenseService.uploadFile(file);
		Food food = this.foodRepo.findById(foodId).get();
		food.setBillPath(publicURL);
		this.foodRepo.save(food);
		Map<String, String> response = new HashMap<>();
		response.put("publicURL", publicURL);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/travel-exp-bill")
	public ResponseEntity<Map<String, String>> uploadtravelExpFile(@RequestParam Integer travelId,
			@RequestParam("file") MultipartFile file) {
		String publicURL = expenseService.uploadFile(file);
		Travel travel = this.travelRepo.findById(travelId).get();
		travel.setBillPath(publicURL);
		this.travelRepo.save(travel);
		Map<String, String> response = new HashMap<>();
		response.put("publicURL", publicURL);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/accmd-exp-bill")
	public ResponseEntity<Map<String, String>> uploadAccomodationExpFile(@RequestParam Integer accmdId,
			@RequestParam("file") MultipartFile file) {
		String publicURL = expenseService.uploadFile(file);
		Accommodation accommodation = this.accomodationRepo.findById(accmdId).get();
		accommodation.setBillPath(publicURL);
		this.accomodationRepo.save(accommodation);
		Map<String, String> response = new HashMap<>();
		response.put("publicURL", publicURL);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/other-exp-bill")
	public ResponseEntity<Map<String, String>> uploadOtherExpFile(@RequestParam Integer otherExpId,
			@RequestParam("file") MultipartFile file) {
		String publicURL = expenseService.uploadFile(file);
		OtherExpenses otherExpenses = this.otherExpensesRepo.findById(otherExpId).get();
		otherExpenses.setBillPath(publicURL);
		this.otherExpensesRepo.save(otherExpenses);
		Map<String, String> response = new HashMap<>();
		response.put("publicURL", publicURL);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
	}

	// UMER
	@PostMapping("/allowance-exp-letter")
	public ResponseEntity<Map<String, String>> uploadAllowanceExpFile(@RequestParam Integer allowanceId,
			@RequestParam("file") MultipartFile file) {
		String publicURL = expenseService.uploadFile(file);
		Allowance allowance = this.allowanceRepo.findById(allowanceId).get();
		allowance.setLetterPath(publicURL);
		this.allowanceRepo.save(allowance);
		Map<String, String> response = new HashMap<>();
		response.put("publicURL", publicURL);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.CREATED);
	}

	//UMER
	@PostMapping("/read-notification")
	public ResponseEntity<?> readNotification(Principal principal) {

		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());

		List<ExpenseNotification> notification = this.expenseNotificatonRepo.findByEmpIdFk(employee.getEmpId());

		for (ExpenseNotification noti : notification) {
			noti.setFlag(false);
			this.expenseNotificatonRepo.save(noti);
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	//UMER
	@PostMapping("/notification")
	public ResponseEntity<?> Notification(Principal principal) {

		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(principal.getName());

		List<ExpenseNotification> notification = this.expenseNotificatonRepo.findByMasterEmployeeDetailsAndFlag(employee,true);
		
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd,MMMM,yyyy : hh:mm a",Locale.ENGLISH);
		 
	    List<NotificationResponse> resList =	notification.stream().map(noti->{
			NotificationResponse res=new NotificationResponse();
			res.setCreatedAt(dtf.format(noti.getCreatedAt()));
			res.setFlag(noti.isFlag());
			res.setMessage(noti.getMessage());
			res.setNotificationId(noti.getNotificationId());
			return res;
			}).collect(Collectors.toList());
			
		return new ResponseEntity<>(resList,HttpStatus.OK); 
	}
	
	

}
