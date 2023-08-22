package ls.lesm.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import ls.lesm.exception.DateMissMatchException;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.exp.Accommodation;
import ls.lesm.model.exp.Allowance;
import ls.lesm.model.exp.ExpenseNotification;
import ls.lesm.model.exp.FinanceApprovalStatus;
import ls.lesm.model.exp.Food;
import ls.lesm.model.exp.ManagerApprovalStatus;
import ls.lesm.model.exp.OtherExpenses;
import ls.lesm.model.exp.Status;
import ls.lesm.model.exp.TotalEmpExpenses;
import ls.lesm.model.exp.TotalFinanceExpenses;
import ls.lesm.model.exp.TotalManagerExpenses;
import ls.lesm.model.exp.Travel;
import ls.lesm.payload.request.ApprovalStatusRequest;
import ls.lesm.payload.request.ExpensesRequest;
import ls.lesm.payload.response.AllExpensesResponse;
import ls.lesm.payload.response.InitialExpenseDetailResponse;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.expRepo.AccomodationRepo;
import ls.lesm.repository.expRepo.AllowanceRepo;
import ls.lesm.repository.expRepo.ExpenseNotificatonRepo;
import ls.lesm.repository.expRepo.FinanceApprovalStatusRepo;
import ls.lesm.repository.expRepo.FoodRepo;
import ls.lesm.repository.expRepo.ManagerApprovalStatusRepo;
import ls.lesm.repository.expRepo.OtherExpensesRepo;
import ls.lesm.repository.expRepo.TotalEmpExpensesRepo;
import ls.lesm.repository.expRepo.TotalFinanceExpensesRepo;
import ls.lesm.repository.expRepo.TotalManagerExpensesRepo;
import ls.lesm.repository.expRepo.TravelRepo;
import ls.lesm.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	private SalaryRepository salaryRepository;

	@Autowired
	private AllowanceRepo allowanceRepo;

	@Autowired
	private FoodRepo foodRepo;

	@Autowired
	private TravelRepo travelRepo;

	@Autowired
	private AccomodationRepo accomodationRepo;

	@Autowired
	private OtherExpensesRepo otherExpensesRepo;

	@Autowired
	private TotalEmpExpensesRepo totalEmpExpensesRepo;

	@Autowired
	private ManagerApprovalStatusRepo managerApprovalStatusRepo;

	@Autowired
	private FinanceApprovalStatusRepo financeApprovalStatusRepo;

	@Autowired
	private ExpenseNotificatonRepo expenseNotificatonRepo;

	@Autowired
	private TotalManagerExpensesRepo totalManagerExpensesRepo;

	@Autowired
	private TotalFinanceExpensesRepo totalFinanceExpensesRepo;

	@Autowired
	private AmazonS3Client awsS3Client;

	//UMER
	@Override
	public Salary inserSal(Salary sal, Principal principal, Integer empId) {
		sal.setCreatedAt(LocalDate.now());
		sal.setCreatedBy(principal.getName());
		this.masterEmployeeDetailsRepository.findById(empId).map(id -> {
			sal.setMasterEmployeeDetails(id);
			return id;
		});

		return salaryRepository.save(sal);
	}

	//UMER
	@Transactional
	@Override
	public ExpensesRequest insertExpenses(Principal principal, ExpensesRequest expensesResponse)
			throws IllegalArgumentException {
		String loggedInUsername = principal.getName();
		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedInUsername);
		int id = employee.getEmpId();

		TotalEmpExpenses exp = new TotalEmpExpenses();

		exp.setStartDate(expensesResponse.getStartDate());
		exp.setEndDate(expensesResponse.getEndDate());
		exp.setExpenseType(expensesResponse.getExpenseType());

		Double totalFoodAmount = 0.0;
		Double totalAccmdAmount = 0.0;
		Double totalTravelAmount = 0.0;
		Double totalOtherExpAmount = 0.0;
		Double totalAllowanceAmount = 0.0;

		if (expensesResponse.getAllowance() != null) {
			this.masterEmployeeDetailsRepository.findById(id).map(idd -> {
				expensesResponse.getAllowance().setMasterEmployeeDetails(idd);
				return idd;
			});

			ManagerApprovalStatus savedM = this.managerApprovalStatusRepo
					.save(new ManagerApprovalStatus(Status.PENDING, employee));

			this.managerApprovalStatusRepo.findById(savedM.getMApproveId()).map(mId -> {
				expensesResponse.getAllowance().setManagerApprovalStatus(mId);
				return mId;
			});

			FinanceApprovalStatus savedF = this.financeApprovalStatusRepo
					.save(new FinanceApprovalStatus(Status.PENDING, employee));

			this.financeApprovalStatusRepo.findById(savedF.getFApproveId()).map(fId -> {
				expensesResponse.getAllowance().setFinanceApprovalStatus(fId);
				return fId;
			});

		} // if

		if (expensesResponse.getFood() != null) {
			for (Food food : expensesResponse.getFood()) {
				this.masterEmployeeDetailsRepository.findById(id).map(idd -> {
					food.setMasterEmployeeDetails(idd);
					return idd;
				});
				totalFoodAmount += food.getAmount();

				ManagerApprovalStatus savedM = this.managerApprovalStatusRepo
						.save(new ManagerApprovalStatus(Status.PENDING, employee));

				this.managerApprovalStatusRepo.findById(savedM.getMApproveId()).map(mId -> {
					food.setManagerApprovalStatus(mId);
					return mId;
				});

				FinanceApprovalStatus savedF = this.financeApprovalStatusRepo
						.save(new FinanceApprovalStatus(Status.PENDING, employee));

				this.financeApprovalStatusRepo.findById(savedF.getFApproveId()).map(fId -> {
					food.setFinanceApprovalStatus(fId);
					return fId;
				});

				if (food.getDate().isAfter(exp.getEndDate()) || food.getDate().isBefore(exp.getStartDate()))
					throw new DateMissMatchException("Please add dates between start date and end date ");

			} // for(food)

			exp.setFood(totalFoodAmount);
		} // if

		if (expensesResponse.getTravel() != null) {
			for (Travel travel : expensesResponse.getTravel()) {
				this.masterEmployeeDetailsRepository.findById(id).map(idd -> {
					travel.setMasterEmployeeDetails(idd);
					return idd;
				});

				totalTravelAmount += travel.getAmount();

				if (travel.getTravelDate().isAfter(exp.getEndDate())
						|| travel.getTravelDate().isBefore(exp.getStartDate()))
					throw new DateMissMatchException("Please add dates between start date and end date");

				ManagerApprovalStatus saved = this.managerApprovalStatusRepo
						.save(new ManagerApprovalStatus(Status.PENDING, employee));

				this.managerApprovalStatusRepo.findById(saved.getMApproveId()).map(mId -> {
					travel.setManagerApprovalStatus(mId);
					return mId;
				});

				FinanceApprovalStatus savedF = this.financeApprovalStatusRepo
						.save(new FinanceApprovalStatus(Status.PENDING, employee));

				this.financeApprovalStatusRepo.findById(savedF.getFApproveId()).map(fId -> {
					travel.setFinanceApprovalStatus(fId);
					return fId;
				});

			} // for(travel)
			exp.setTravel(totalTravelAmount);
		} // if

		if (expensesResponse.getOtherExpenses() != null) {
			for (OtherExpenses otherExpenses : expensesResponse.getOtherExpenses()) {
				this.masterEmployeeDetailsRepository.findById(id).map(idd -> {
					otherExpenses.setMasterEmployeeDetails(idd);
					return idd;
				});

				ManagerApprovalStatus saved = this.managerApprovalStatusRepo
						.save(new ManagerApprovalStatus(Status.PENDING, employee));

				this.managerApprovalStatusRepo.findById(saved.getMApproveId()).map(mId -> {
					otherExpenses.setManagerApprovalStatus(mId);
					return mId;
				});

				FinanceApprovalStatus savedF = this.financeApprovalStatusRepo
						.save(new FinanceApprovalStatus(Status.PENDING, employee));

				this.financeApprovalStatusRepo.findById(savedF.getFApproveId()).map(fId -> {
					otherExpenses.setFinanceApprovalStatus(fId);
					return fId;
				});

				if (otherExpenses.getStartDate().isAfter(otherExpenses.getEndDate()))
					throw new DateMissMatchException(
							"Start date can not be after end date, please enter valid date");

				if (otherExpenses.getStartDate().isAfter(LocalDate.now())
						|| otherExpenses.getEndDate().isAfter(LocalDate.now()))
					throw new DateMissMatchException("Future dates is not allowed");

				if (otherExpenses.getStartDate().isAfter(exp.getEndDate())
						|| otherExpenses.getStartDate().isBefore(exp.getStartDate())
						|| otherExpenses.getEndDate().isAfter(exp.getEndDate())
						|| otherExpenses.getEndDate().isBefore(exp.getStartDate()))
					throw new DateMissMatchException(
							"Other expenses start and end dates should be in between start date and end date");

				totalOtherExpAmount += otherExpenses.getAmount();
			} // for(otherExpenses)
			exp.setOthers(totalOtherExpAmount);
		} // if

		if (expensesResponse.getAccommodation() != null) {
			for (Accommodation accommodation : expensesResponse.getAccommodation()) {
				this.masterEmployeeDetailsRepository.findById(id).map(idd -> {
					accommodation.setMasterEmployeeDetails(idd);
					return idd;
				});

				ManagerApprovalStatus saved = this.managerApprovalStatusRepo
						.save(new ManagerApprovalStatus(Status.PENDING, employee));

				this.managerApprovalStatusRepo.findById(saved.getMApproveId()).map(mId -> {
					accommodation.setManagerApprovalStatus(mId);
					return mId;
				});

				FinanceApprovalStatus savedF = this.financeApprovalStatusRepo
						.save(new FinanceApprovalStatus(Status.PENDING, employee));

				this.financeApprovalStatusRepo.findById(savedF.getFApproveId()).map(fId -> {
					accommodation.setFinanceApprovalStatus(fId);
					return fId;
				});

				if (accommodation.getCheckIn().isAfter(accommodation.getCheckOut()))
					throw new DateMissMatchException(
							"Check-in date can not be after check-out date, please enter valid date");

				if (accommodation.getCheckIn().isAfter(LocalDate.now())
						|| accommodation.getCheckOut().isAfter(LocalDate.now()))
					throw new DateMissMatchException("Future dates is not allowed");

				if (accommodation.getCheckIn().isAfter(exp.getEndDate())
						|| accommodation.getCheckIn().isBefore(exp.getStartDate())
						|| accommodation.getCheckOut().isAfter(exp.getEndDate())
						|| accommodation.getCheckOut().isBefore(exp.getStartDate()))
					throw new DateMissMatchException(
							"Hotels check-in and check-out dates should be in between start date and end date");

				accommodation
						.setTotalDays(ChronoUnit.DAYS.between(accommodation.getCheckIn(), accommodation.getCheckOut()));

				totalAccmdAmount += accommodation.getAmount();
			} // for(accommodation)
			exp.setAccomodation(totalAccmdAmount);
		} // if

		// setting allowance based on applying expense start date to end date
		Long allowanceDays = ChronoUnit.DAYS.between(exp.getStartDate(), exp.getEndDate());
		totalAllowanceAmount = allowanceDays * expensesResponse.getAllowance().getAmount();
		exp.setAllowance(totalAllowanceAmount);

		exp.setTotal(
				totalOtherExpAmount + totalFoodAmount + totalAccmdAmount + totalTravelAmount + totalAllowanceAmount);

		this.masterEmployeeDetailsRepository.findById(id).map(idd -> {
			exp.setMasterEmployeeDetails(idd);
			return idd;
		});

		// saved total expenses here because i am using this total expenses id as
		// foreign key
		// to link this total expense to all expenses
		// because of this reason i have taken another for loop for (after saving the
		// record
		// then only we will get the primary auto increment id)
		// for every condition i have taken if statement(it was throwing
		// nullPointerExcption) because there may be a possibility that somebody will
		// not
		// take accommodation if he is going for one day and otherexpense is not
		// compulsary so to get rid from NullPointerException i have taken to many if
		// statemenet

		TotalEmpExpenses savedExpense = this.totalEmpExpensesRepo.save(exp);

		TotalManagerExpenses totalManagerExpenses = new TotalManagerExpenses();
		TotalFinanceExpenses totalFinanceExpenses = new TotalFinanceExpenses();
		// setting zero's to avoid NullPoniterException
		totalManagerExpenses.setAccomodation(0.0);
		totalManagerExpenses.setAllowance(0.0);
		totalManagerExpenses.setFood(0.0);
		totalManagerExpenses.setOthers(0.0);
		totalManagerExpenses.setTravel(0.0);
		totalManagerExpenses.setTotal(0.0);

		totalFinanceExpenses.setAccomodation(0.0);
		totalFinanceExpenses.setAllowance(0.0);
		totalFinanceExpenses.setFood(0.0);
		totalFinanceExpenses.setOthers(0.0);
		totalFinanceExpenses.setTravel(0.0);
		totalFinanceExpenses.setTotal(0.0);

		this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tId -> {
			totalManagerExpenses.setTotalEmpExpenses(tId);
			return tId;
		});

		this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tId -> {
			totalFinanceExpenses.setTotalEmpExpenses(tId);
			return tId;
		});

		this.masterEmployeeDetailsRepository.findById(id).map(eId -> {
			totalManagerExpenses.setMasterEmployeeDetails(eId);
			return eId;
		});

		this.masterEmployeeDetailsRepository.findById(id).map(eId -> {
			totalFinanceExpenses.setMasterEmployeeDetails(eId);
			return eId;
		});

		totalManagerExpensesRepo.save(totalManagerExpenses);
		totalFinanceExpensesRepo.save(totalFinanceExpenses);

		// ManagerApprovalStatus
		// savedMngerApproval=this.managerApprovalStatusRepo.save(managerApprovalStatus);
		// FinanceApprovalStatus
		// savedFinanceApproval=this.financeApprovalStatusRepo.save(financeApprovalStatus);

		// assigning to all total tbl pk as foreign to all expenses tbl key below

		if (expensesResponse.getFood() != null) {
			for (Food food : expensesResponse.getFood()) {

				this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tExpId -> {
					food.setTotalEmpExpenses(tExpId);
					return tExpId;
				});

			} // for(food)
		} // if
		if (expensesResponse.getTravel() != null) {
			for (Travel travel : expensesResponse.getTravel()) {
				this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tExpId -> {
					travel.setTotalEmpExpenses(tExpId);
					return tExpId;
				});

			} // for(travel)
		} // if
		if (expensesResponse.getOtherExpenses() != null) {
			for (OtherExpenses otherExpenses : expensesResponse.getOtherExpenses()) {

				this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tExpId -> {
					otherExpenses.setTotalEmpExpenses(tExpId);
					return tExpId;
				});

			} // for(otherExp)
		} // if
		if (expensesResponse.getAccommodation() != null) {
			for (Accommodation accommodation : expensesResponse.getAccommodation()) {

				this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tExpId -> {
					accommodation.setTotalEmpExpenses(tExpId);
					return tExpId;
				});

			} // for(accommodation)
		} // if
		this.totalEmpExpensesRepo.findById(savedExpense.getTotalEmpExpId()).map(tExpId -> {
			expensesResponse.getAllowance().setTotalEmpExpenses(tExpId);
			return tExpId;
		});
		// setting notification here for employee manager
		ExpenseNotification notification = new ExpenseNotification();
		notification.setFlag(true);

		// assigned emp supervisor id for notification
		this.masterEmployeeDetailsRepository.findById(employee.getSupervisor().getEmpId()).map(sId -> {
			notification.setMasterEmployeeDetails(sId);
			return sId;
		});

		notification.setMessage(
				"Hi, You have expense claim request from " + employee.getFirstName() + " " + employee.getLastName());
		this.expenseNotificatonRepo.save(notification);

		// when we pass null object to save method it throw IllegalArgumetnExcpetion
		// "Entity must not be null" thats the reson i have taken if statement here
		if (expensesResponse.getAllowance() != null)
			this.allowanceRepo.save(expensesResponse.getAllowance());

		if (expensesResponse.getFood() != null)
			this.foodRepo.saveAll(expensesResponse.getFood());

		if (expensesResponse.getTravel() != null)
			this.travelRepo.saveAll(expensesResponse.getTravel());

		if (expensesResponse.getOtherExpenses() != null)
			this.otherExpensesRepo.saveAll(expensesResponse.getOtherExpenses());

		if (expensesResponse.getAccommodation() != null)
			this.accomodationRepo.saveAll(expensesResponse.getAccommodation());

		return expensesResponse;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveFoodExpByManager(Principal principal, List<ApprovalStatusRequest> req) {

		Double totalFoodAmount = 0.0;

		for (ApprovalStatusRequest r : req) {
			Food food = this.foodRepo.findById(r.getExpenseId()).get();

			ManagerApprovalStatus status = this.managerApprovalStatusRepo
					.findById(food.getManagerApprovalStatus().getMApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(food.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			ManagerApprovalStatus saved = this.managerApprovalStatusRepo.save(status);
			totalFoodAmount += saved.getApprovedAmount();
			TotalManagerExpenses managerTotal = this.totalManagerExpensesRepo
					.findByTotalIdFk(food.getTotalEmpExpenses().getTotalEmpExpId()).get();
			managerTotal.setFood(totalFoodAmount);
			managerTotal.setTotal(managerTotal.getAccomodation() + managerTotal.getAllowance()
					+ managerTotal.getOthers() + managerTotal.getTravel() + managerTotal.getFood());
			managerTotal.setStatus(true);
			this.totalManagerExpensesRepo.save(managerTotal);
		} // for(req)

		return req;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveFoodExpByFinance(Principal principal, List<ApprovalStatusRequest> req) {

		Double totalFoodAmount = 0.0;

		for (ApprovalStatusRequest r : req) {

			Food food = this.foodRepo.findById(r.getExpenseId()).get();
			FinanceApprovalStatus status = this.financeApprovalStatusRepo
					.findById(food.getFinanceApprovalStatus().getFApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(food.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			FinanceApprovalStatus saved = this.financeApprovalStatusRepo.save(status);
			totalFoodAmount += saved.getApprovedAmount();
			TotalFinanceExpenses financeTotal = this.totalFinanceExpensesRepo
					.findByTotalIdFk(food.getTotalEmpExpenses().getTotalEmpExpId()).get();
			financeTotal.setFood(totalFoodAmount);
			financeTotal.setTotal(financeTotal.getAccomodation() + financeTotal.getAllowance()
					+ financeTotal.getOthers() + financeTotal.getTravel() + financeTotal.getFood());
			financeTotal.setStatus(true);
			this.totalFinanceExpensesRepo.save(financeTotal);

		} // for(req)
		return req;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveTravelExpByManager(Principal principal, List<ApprovalStatusRequest> req) {

		Double totalTravelAmount = 0.0;

		for (ApprovalStatusRequest r : req) {
			Travel travel = this.travelRepo.findById(r.getExpenseId()).get();

			ManagerApprovalStatus status = this.managerApprovalStatusRepo
					.findById(travel.getManagerApprovalStatus().getMApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(travel.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			ManagerApprovalStatus saved = this.managerApprovalStatusRepo.save(status);
			totalTravelAmount += saved.getApprovedAmount();
			TotalManagerExpenses managerTotal = this.totalManagerExpensesRepo
					.findByTotalIdFk(travel.getTotalEmpExpenses().getTotalEmpExpId()).get();
			managerTotal.setTravel(totalTravelAmount);
			managerTotal.setTotal(managerTotal.getAccomodation() + managerTotal.getAllowance()
					+ managerTotal.getOthers() + managerTotal.getTravel() + managerTotal.getFood());
			managerTotal.setStatus(true);
			this.totalManagerExpensesRepo.save(managerTotal);
		} // for(req)

		return req;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveTravelExpByFinance(Principal principal, List<ApprovalStatusRequest> req) {

		Double totalTravelAmount = 0.0;

		for (ApprovalStatusRequest r : req) {
			Travel travel = this.travelRepo.findById(r.getExpenseId()).get();

			FinanceApprovalStatus status = this.financeApprovalStatusRepo
					.findById(travel.getFinanceApprovalStatus().getFApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(travel.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			FinanceApprovalStatus saved = this.financeApprovalStatusRepo.save(status);
			totalTravelAmount += saved.getApprovedAmount();
			TotalFinanceExpenses financeTotal = this.totalFinanceExpensesRepo
					.findByTotalIdFk(travel.getTotalEmpExpenses().getTotalEmpExpId()).get();
			financeTotal.setTravel(totalTravelAmount);
			financeTotal.setTotal(financeTotal.getAccomodation() + financeTotal.getAllowance()
					+ financeTotal.getOthers() + financeTotal.getTravel() + financeTotal.getFood());
			financeTotal.setStatus(true);
			this.totalFinanceExpensesRepo.save(financeTotal);
		} // for(req)

		return req;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveAccomodationExpByManager(Principal principal,
			List<ApprovalStatusRequest> req) {

		Double totalAccmdAmount = 0.0;

		for (ApprovalStatusRequest r : req) {
			Accommodation accomodation = this.accomodationRepo.findById(r.getExpenseId()).get();

			ManagerApprovalStatus status = this.managerApprovalStatusRepo
					.findById(accomodation.getManagerApprovalStatus().getMApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(accomodation.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			ManagerApprovalStatus saved = this.managerApprovalStatusRepo.save(status);
			totalAccmdAmount += saved.getApprovedAmount();
			TotalManagerExpenses managerTotal = this.totalManagerExpensesRepo
					.findByTotalIdFk(accomodation.getTotalEmpExpenses().getTotalEmpExpId()).get();

			managerTotal.setAccomodation(totalAccmdAmount);

			managerTotal.setTotal(managerTotal.getAccomodation() + managerTotal.getAllowance()
					+ managerTotal.getOthers() + managerTotal.getTravel() + managerTotal.getFood());
			managerTotal.setStatus(true);
			this.totalManagerExpensesRepo.save(managerTotal);

		} // for(req)

		return req;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveAccomodationExpByFinance(Principal principal,
			List<ApprovalStatusRequest> req) {

		Double totalAccmdAmount = 0.0;

		for (ApprovalStatusRequest r : req) {
			Accommodation accomodation = this.accomodationRepo.findById(r.getExpenseId()).get();

			FinanceApprovalStatus status = this.financeApprovalStatusRepo
					.findById(accomodation.getFinanceApprovalStatus().getFApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(accomodation.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			FinanceApprovalStatus saved = this.financeApprovalStatusRepo.save(status);
			totalAccmdAmount += saved.getApprovedAmount();
			TotalFinanceExpenses financeTotal = this.totalFinanceExpensesRepo
					.findByTotalIdFk(accomodation.getTotalEmpExpenses().getTotalEmpExpId()).get();
			financeTotal.setAccomodation(totalAccmdAmount);
			financeTotal.setTotal(financeTotal.getAccomodation() + financeTotal.getAllowance()
					+ financeTotal.getOthers() + financeTotal.getTravel() + financeTotal.getFood());
			financeTotal.setStatus(true);
			this.totalFinanceExpensesRepo.save(financeTotal);
		} // for(req)

		return req;
	}// method

	//UMER
	@Override
	public ApprovalStatusRequest approveAllowanceExpByManager(Principal principal, ApprovalStatusRequest req) {

		Double totalAllowanceAmount = 0.0;

		Allowance allowance = this.allowanceRepo.findById(req.getExpenseId()).get();
		ManagerApprovalStatus mStatus = this.managerApprovalStatusRepo
				.findById(allowance.getManagerApprovalStatus().getMApproveId()).get();

		TotalEmpExpenses empTotal = this.totalEmpExpensesRepo
				.findById(allowance.getTotalEmpExpenses().getTotalEmpExpId()).get();
		mStatus.setStatus(req.getStatus());
		mStatus.setApprovedBy(principal.getName());
		mStatus.setRemark(req.getRemark());
		if (req.getStatus() == Status.APPROVED) {
			mStatus.setApprovedAmount(empTotal.getAllowance());
			totalAllowanceAmount = empTotal.getAllowance();
		}
		if (req.getStatus() == Status.REJECTED) {
			mStatus.setApprovedAmount(0.0);
			totalAllowanceAmount = 0.0;
		}
		if (req.getStatus() == Status.PARTIAL_APPROVED) {
			mStatus.setApprovedAmount(req.getApprovedAmount());
			totalAllowanceAmount = req.getApprovedAmount();
		}
		ManagerApprovalStatus saved = this.managerApprovalStatusRepo.save(mStatus);

		TotalManagerExpenses managerTotal = this.totalManagerExpensesRepo
				.findByTotalIdFk(allowance.getTotalEmpExpenses().getTotalEmpExpId()).get();
		managerTotal.setAllowance(totalAllowanceAmount);
		managerTotal.setTotal(managerTotal.getAccomodation() + managerTotal.getAllowance() + managerTotal.getOthers()
				+ managerTotal.getTravel() + managerTotal.getFood());
		managerTotal.setStatus(true);
		this.totalManagerExpensesRepo.save(managerTotal);
		return req;
	}

	//UMER
	@Override
	public ApprovalStatusRequest approveAllowanceExpByFinance(Principal principal, ApprovalStatusRequest req) {

		Double totalAllowanceAmount = 0.0;

		Allowance allowance = this.allowanceRepo.findById(req.getExpenseId()).get();
		FinanceApprovalStatus fStatus = this.financeApprovalStatusRepo
				.findById(allowance.getFinanceApprovalStatus().getFApproveId()).get();
		TotalEmpExpenses empTotal = this.totalEmpExpensesRepo
				.findById(allowance.getTotalEmpExpenses().getTotalEmpExpId()).get();

		fStatus.setStatus(req.getStatus());
		fStatus.setApprovedBy(principal.getName());
		fStatus.setRemark(req.getRemark());
		if (req.getStatus() == Status.APPROVED) {
			fStatus.setApprovedAmount(empTotal.getAllowance());
			totalAllowanceAmount = empTotal.getAllowance();
		}
		if (req.getStatus() == Status.REJECTED) {
			fStatus.setApprovedAmount(0.0);
			totalAllowanceAmount = 0.0;
		}
		if (req.getStatus() == Status.PARTIAL_APPROVED) {
			fStatus.setApprovedAmount(req.getApprovedAmount());
			totalAllowanceAmount = req.getApprovedAmount();
		}
		FinanceApprovalStatus saved = this.financeApprovalStatusRepo.save(fStatus);

		TotalFinanceExpenses financeTotal = this.totalFinanceExpensesRepo
				.findByTotalIdFk(allowance.getTotalEmpExpenses().getTotalEmpExpId()).get();
		financeTotal.setAllowance(totalAllowanceAmount);
		financeTotal.setTotal(financeTotal.getTotal() + financeTotal.getAllowance());
		financeTotal.setStatus(true);
		totalFinanceExpensesRepo.save(financeTotal);

		return req;
	}

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveOtherExpByManager(Principal principal, List<ApprovalStatusRequest> req) {

		Double totalOtherExpAmount = 0.0;

		for (ApprovalStatusRequest r : req) {
			OtherExpenses otherExp = this.otherExpensesRepo.findById(r.getExpenseId()).get();

			ManagerApprovalStatus status = this.managerApprovalStatusRepo
					.findById(otherExp.getManagerApprovalStatus().getMApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(otherExp.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			ManagerApprovalStatus saved = this.managerApprovalStatusRepo.save(status);
			totalOtherExpAmount += saved.getApprovedAmount();
			TotalManagerExpenses managerTotal = this.totalManagerExpensesRepo
					.findByTotalIdFk(otherExp.getTotalEmpExpenses().getTotalEmpExpId()).get();
			managerTotal.setOthers(totalOtherExpAmount);
			managerTotal.setTotal(managerTotal.getAccomodation() + managerTotal.getAllowance()
					+ managerTotal.getOthers() + managerTotal.getTravel() + managerTotal.getFood());
			managerTotal.setStatus(true);
			this.totalManagerExpensesRepo.save(managerTotal);
		} // for(req)

		return req;
	}// method

	//UMER
	@Override
	public List<ApprovalStatusRequest> approveOtherExpByFinance(Principal principal, List<ApprovalStatusRequest> req) {

		Double totalOtherExpAmount = 0.0;

		for (ApprovalStatusRequest r : req) {

			OtherExpenses otherExp = this.otherExpensesRepo.findById(r.getExpenseId()).get();
			FinanceApprovalStatus status = this.financeApprovalStatusRepo
					.findById(otherExp.getFinanceApprovalStatus().getFApproveId()).get();

			status.setStatus(r.getStatus());
			status.setApprovedBy(principal.getName());
			status.setRemark(r.getRemark());
			if (r.getStatus() == Status.APPROVED)
				status.setApprovedAmount(otherExp.getAmount());

			if (r.getStatus() == Status.REJECTED)
				status.setApprovedAmount(0.0);

			if (r.getStatus() == Status.PARTIAL_APPROVED)
				status.setApprovedAmount(r.getApprovedAmount());

			FinanceApprovalStatus saved = this.financeApprovalStatusRepo.save(status);
			totalOtherExpAmount += saved.getApprovedAmount();
			TotalFinanceExpenses financeTotal = this.totalFinanceExpensesRepo
					.findByTotalIdFk(otherExp.getTotalEmpExpenses().getTotalEmpExpId()).get();
			financeTotal.setOthers(totalOtherExpAmount);
			financeTotal.setTotal(financeTotal.getAccomodation() + financeTotal.getAllowance()
					+ financeTotal.getOthers() + financeTotal.getTravel() + financeTotal.getFood());
			financeTotal.setStatus(true);
			this.totalFinanceExpensesRepo.save(financeTotal);

		} // for(req)
		return req;
	}// method

	//UMER
	@Override
	public List<InitialExpenseDetailResponse> getCardDetails(Principal principal) {
		String loggedInUsername = principal.getName();
		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedInUsername);

		return this.masterEmployeeDetailsRepository.getCardDetailOfExpenses(employee.getEmpId());
	}

	//UMER
	@Override
	public AllExpensesResponse getAllExpByTotalId(Integer totalId) {
		AllExpensesResponse all = new AllExpensesResponse();
		all.setTotalEmpExpenses(this.totalEmpExpensesRepo.findById(totalId));
		all.setOtherExpenses(this.otherExpensesRepo.findByTotalIdFk(totalId));
		all.setAccommodation(this.accomodationRepo.findByTotalIdFk(totalId));
		all.setAllowance(this.allowanceRepo.findByTotalIdFk(totalId));
		all.setTravel(this.travelRepo.findByTotalIdFk(totalId));
		all.setFood(this.foodRepo.findByTotalIdFk(totalId));

		return all;
	}

	//UMER
	@Override
	public String uploadFile(MultipartFile file) {

		String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

		String key = UUID.randomUUID().toString() + "." + filenameExtension;

		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentLength(file.getSize());
		metaData.setContentType(file.getContentType());

		try {
			awsS3Client.putObject("lesm", key, file.getInputStream(), metaData);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An exception occured while uploading the file");
		}

		awsS3Client.setObjectAcl("lesm", key, CannedAccessControlList.PublicRead);

		return awsS3Client.getResourceUrl("lesm", key);
	}

}
