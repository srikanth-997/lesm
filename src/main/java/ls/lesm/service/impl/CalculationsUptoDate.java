package ls.lesm.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.AtClientAllowances;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ReleaseEmpDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.exp.TotalFinanceExpenses;
import ls.lesm.repository.AllowancesOfEmployeeRepo;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.expRepo.TotalFinanceExpensesRepo;

@Service
public class CalculationsUptoDate {

	@Autowired
	private EmployeesAtClientsDetailsRepository clientDetail;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepo;

	@Autowired
	ReleaseEmpDetailsRepository releaseEmpDetailsRepository;

	@Autowired
	TotalFinanceExpensesRepo totalFinanceExpensesRepository;

	@Autowired
	InternalExpensesRepository internalExpenseRepo;

	@Autowired
	private SalaryRepository salaryRepo;

	@Autowired
	private SubDepartmentsRepository sdr;

	@Autowired
	AllowancesOfEmployeeRepo allowancesOfEmployeeRepo;

	public synchronized double lesmCalculations(int employeeId, LocalDate fromDate, LocalDate toDate)

	{

		double profit_or_los = 0.0;
		long Total_internal_tenure = 0l;
		double Total_expenses = 0l;// u
		long tenure = 0l;
		long TotalDays = 0l;
		Double Total_internal_pay = 0.0;
		Double totalFinanceExp = 0.0;
		double paid_till_now = 0l;
		double remaining_days = 0;
		double perday = 0;
		long TotaldaysAll=0l;

		int inc = 0;

		Integer Total_client_tenure[] = { 0 };// u
		long Total_client_days[] = { 0 };// u
		double Total_salary_from_client[] = { 0l };// u

		try {

			if (fromDate == null && toDate == null) {
				throw new NullPointerException(" No values present, default will be Complete Calculation");

			}

			else {
				fromDate = LocalDate.of(fromDate.getYear(), fromDate.getMonthValue(), 1);

			}

		} catch (Exception e) {

			toDate = LocalDate.now();

			try {
				fromDate = masterEmployeeDetailsRepo.findById(employeeId).get().getJoiningDate();

			} catch (Exception joiningdate) {
			//	System.err.println("Joining date not present");
				return 0;

			}

		}

		MasterEmployeeDetails MasterEmployee = masterEmployeeDetailsRepo.findById(employeeId).get();

		List<Salary> salary = salaryRepo.findsBymasterEmployeeDetails_Id(employeeId);

		if (salary.isEmpty()) {

			System.err.println("Salary error");

			return 0;

		}

		int salaryRecords = salary.size();

		if (salaryRecords == 1)// number_of_salary_records_of_single_employee==1
		{

			LocalDate Date1 = MasterEmployee.getJoiningDate();

			LocalDate Date2 = LocalDate.now();
			;

			if (MasterEmployee.getStatus() != EmployeeStatus.EXIT
					|| MasterEmployee.getStatus() != EmployeeStatus.TERMINATED
					|| MasterEmployee.getStatus() != EmployeeStatus.ABSCONDED)

			{

				Date2 = LocalDate.now();

			}

			else {

				Date2 = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(employeeId).get().getReleaseDate();

				if (Date2 == null) {
					System.err.println("Release date Error");
					return 0;

				}

			}

			if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

				if (Date1.isBefore(fromDate)) {

					Date1 = fromDate;

				}
				if (Date2.isAfter(toDate)) {

					Date2 = toDate;

				}

				tenure = ChronoUnit.MONTHS.between(Date1, Date2);

				TotalDays = ChronoUnit.DAYS.between(Date1, Date2);

				for (Salary s : salary) {

					perday = s.getSalary() / 30;

					paid_till_now = TotalDays * perday;

					Optional<InternalExpenses> inten = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId);

					InternalExpenses expenses = null;

					if (!inten.isPresent()) {

						System.err.println("Am creating the record");

						internalExpenseRepo.save(new InternalExpenses(MasterEmployee));
						expenses = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId).get();

					} else {
						expenses = inten.get();

					}

					expenses.setTotalSalPaidTillNow(Math.ceil(paid_till_now));

					clientCalculation(employeeId, fromDate, toDate, Total_client_tenure, Total_salary_from_client,
							Total_client_days);

					long Bench_tenure = tenure - Total_client_tenure[0];

					expenses.setBenchTenure(TotalDays);
					long daysOnBench = TotalDays - Total_client_days[0];
					expenses.setDaysOnBench(daysOnBench);
					double benchpay = daysOnBench * perday;
					expenses.setBenchPay(Math.ceil(benchpay));

					expenses.setBR_INR(Math.ceil(Total_salary_from_client[0]));

					expenses.setBR_USD(Math.ceil(Total_salary_from_client[0] / 74));

					expenses.setPR_INR(Math.ceil(paid_till_now));

					expenses.setPR_USD(Math.ceil(paid_till_now / 74));

					expenses.setGPM_INR(Math.ceil(Total_salary_from_client[0] - paid_till_now));

					expenses.setGPM_USD(Math.ceil((Total_salary_from_client[0] / 74 - (paid_till_now / 74))));

					expenses.setGM(Math.ceil(Total_salary_from_client[0] / paid_till_now));

					List<TotalFinanceExpenses> totalFinanceExpenses = totalFinanceExpensesRepository
							.findBymasterEmployeeDetails_Id(employeeId);

					if (!totalFinanceExpenses.isEmpty()) {

						for (TotalFinanceExpenses tf : totalFinanceExpenses) {

							totalFinanceExp += tf.getTotal();

						}
					}
					try {

						profit_or_los = Total_salary_from_client[0]
								- (paid_till_now + totalFinanceExp + allowancesOfEmployee(employeeId, TotalDays));
					} catch (Exception e) {
						profit_or_los = Total_salary_from_client[0] - (paid_till_now + totalFinanceExp + 0.0);
					}

//					System.out.println("EmpId" + employeeId);
//					System.out.println("salary Per Day:" + perday);
//					System.out.println("Date-1" + Date1 + " Date-2" + Date2 + " Total number of days:" + TotalDays);
//					System.out.println("Pail Till Now" + paid_till_now);
//					System.out.println("Profit/Loss" + profit_or_los);
					try {
						expenses.setTotalExpenses(
								Math.ceil(paid_till_now + allowancesOfEmployee(employeeId, TotalDays)));
					} catch (Exception e) {
						expenses.setTotalExpenses(Math.ceil(paid_till_now + 0.0));
					}
					expenses.setProfitOrLoss(Math.ceil(profit_or_los));

					internalExpenseRepo.save(expenses);

					break;

				}

			}

		}

		else {

			LocalDate Date1 = null;
			LocalDate Date2 = null;
			long i = 0;

			Double tempsal = 0.0;

			for (Salary resolvenull : salary) {
				if (resolvenull.getUpdatedAt() == null) {

					int index = salary.indexOf(resolvenull);

					resolvenull.setUpdatedAt(MasterEmployee.getJoiningDate());

					salary.set(index, resolvenull);

				}

			}

			salary.sort(Comparator.comparing(Salary::getUpdatedAt));

			for (Salary resetnull : salary) {
				if (resetnull.getUpdatedAt().equals(MasterEmployee.getJoiningDate())) {

					int index = salary.indexOf(resetnull);

					resetnull.setUpdatedAt(null);

					salary.set(index, resetnull);

				}

			}

			for (Salary record : salary) {
				if (Date1 == null) {

					Date1 = MasterEmployee.getJoiningDate();

				}

				Date2 = record.getUpdatedAt();

				if (Date2 == null) {
					Date2 = LocalDate.now();
				}

				Double sal = record.getSalary();

				if (i == 0) {
					tempsal = sal;
					i++;
					continue;
				}

				//System.out.println(Date2);

				if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

					if (Date1.isBefore(fromDate)) {

						Date1 = fromDate;

					}

					if (Date2.isAfter(toDate)) {

						Date2 = toDate;

					}

					tenure = ChronoUnit.MONTHS.between(Date1, Date2);

					TotalDays = ChronoUnit.DAYS.between(Date1, Date2);

					Total_internal_tenure += tenure;
					TotaldaysAll+=TotalDays;

					double perdaysalary = tempsal / 30;

					paid_till_now = TotalDays * perdaysalary;

					Total_internal_pay += paid_till_now;

				}

				Date1 = Date2;
				tempsal = record.getSalary();
			}

			if (MasterEmployee.getStatus() != EmployeeStatus.EXIT
					&& MasterEmployee.getStatus() != EmployeeStatus.ABSCONDED
					&& MasterEmployee.getStatus() != EmployeeStatus.TERMINATED)

			{

				Date2 = LocalDate.now();

			}

			else {

				Date2 = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(employeeId).get().getReleaseDate();

				if (Date2 == null) {

					return 0;
				}

				// Date2 = releaseDate;

			}

			if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

				if (Date1.isBefore(fromDate)) {

					Date1 = fromDate;

				}

				if (Date2.isAfter(toDate)) {

					Date2 = toDate;

				}

				tenure = ChronoUnit.MONTHS.between(Date1, Date2);

				TotalDays = ChronoUnit.DAYS.between(Date1, Date2);

				Total_internal_tenure += tenure;
				TotaldaysAll+=TotalDays;

				perday = tempsal / 30;

				paid_till_now = TotalDays * perday;

				Total_internal_pay += paid_till_now;

				Optional<InternalExpenses> inten = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId);

				InternalExpenses expenses = null;

				if (!inten.isPresent()) {

					// System.out.println("Am creating the record");
					// new InternalExpenses(MasterEmployee);
					internalExpenseRepo.save(new InternalExpenses(MasterEmployee));
					expenses = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId).get();

				} else {
					expenses = inten.get();

				}

				try {

					expenses.setTotalSalPaidTillNow(Math.ceil(Total_internal_pay));

					clientCalculation(employeeId, fromDate, toDate, Total_client_tenure, Total_salary_from_client,
							Total_client_days);

//					long Bench_tenure = tenure - Total_client_tenure[0];
//
//					expenses.setBenchTenure(TotalDays);

					List<TotalFinanceExpenses> totalFinanceExpenses = totalFinanceExpensesRepository
							.findBymasterEmployeeDetails_Id(employeeId);

					for (TotalFinanceExpenses tf : totalFinanceExpenses) {

						totalFinanceExp += tf.getTotal();

					}

					long Bench_tenure = tenure - Total_client_tenure[0];

					expenses.setBenchTenure(TotaldaysAll);
					long daysOnBench = TotaldaysAll - Total_client_days[0];
				//	System.out.println(daysOnBench+" day son bench "+TotalDays+" Total days "+Total_client_days[0]+" client days ");
					expenses.setDaysOnBench(-daysOnBench);
					double benchpay = daysOnBench * perday;
					expenses.setBenchPay(Math.ceil(-benchpay));

					expenses.setBR_INR(Math.ceil(Total_salary_from_client[0]));

					expenses.setBR_USD(Math.ceil(Total_salary_from_client[0] / 74));

					expenses.setPR_INR(Math.ceil(paid_till_now));

					expenses.setPR_USD(Math.ceil(paid_till_now / 74));

					expenses.setGPM_INR(Math.ceil(Total_salary_from_client[0] - paid_till_now));

					expenses.setGPM_USD(Math.ceil((Total_salary_from_client[0] / 74 - (paid_till_now / 74))));

					expenses.setGM(Math.ceil(Total_salary_from_client[0] / paid_till_now));

					
				//	System.out.println(benchpay+" ...........................");
				//	expenses.setBenchPay(Math.ceil(benchpay));
					
					
					expenses.setGPM_USD(
							(Math.ceil(Total_salary_from_client[0] / 74) - Math.ceil(Total_internal_pay / 74)));

					expenses.setGM(Math.ceil(Total_salary_from_client[0] / Total_internal_pay));
					try {
						profit_or_los = Total_salary_from_client[0]
								- (Total_internal_pay + totalFinanceExp + allowancesOfEmployee(employeeId, TotalDays));
						
						
					} catch (Exception e) {
						profit_or_los = Total_salary_from_client[0] - (Total_internal_pay + totalFinanceExp + 0.0);
					}

					//System.out.println("EmpId" + employeeId);
					//System.out.println("salary Per Day:" + perday);
					//System.out.println("Date-1" + Date1 + " Date-2" + Date2 + " Total number of days:" + TotalDays);

				//	System.out.println("Profit/Loss" + profit_or_los);
					try {
						expenses.setTotalExpenses(
								Math.ceil(Total_internal_pay + allowancesOfEmployee(employeeId, TotalDays)));
					} catch (Exception e) {

					}
					expenses.setProfitOrLoss(Math.ceil(profit_or_los));

				} catch (Exception e) {

				//	System.err.println("TotalFinanceExpenses");
					return 0;
				}

				internalExpenseRepo.save(expenses);

			}

		}
		//System.out.println("Total internal par" + Total_internal_pay);

		return profit_or_los;

	}

	public void clientCalculation(int employeeId, LocalDate fromDate, LocalDate toDate, Integer Total_client_tenure[],
			double Total_salary_from_client[], long Total_client_days[]) {

		List<EmployeesAtClientsDetails> slidet = clientDetail.findsBymasterEmployeeDetails_Id(employeeId);

		if (!slidet.isEmpty())

		{

			for (EmployeesAtClientsDetails cl : slidet) {

				LocalDate PoSdate = cl.getPOSdate();

				LocalDate PoEdate = cl.getPOEdate();

				if (cl.getClientJoiningDate() != null) {
					PoSdate = cl.getClientJoiningDate();

				}
				if (cl.getClientLastWorkingDate() != null) {
					PoEdate = cl.getClientLastWorkingDate();
				}

				LocalDate systemdate = LocalDate.now();
				try {
					if (PoEdate == null || systemdate.isBefore(PoEdate)) {

						PoEdate = LocalDate.now();

					}
				}

				catch (Exception e) {
					PoEdate = LocalDate.now();

				}

				if (PoSdate.isBefore(toDate) && PoEdate.isAfter(fromDate)) {

					if (PoSdate.isBefore(fromDate)) {

						PoSdate = fromDate;

					}

					if (PoEdate.isAfter(toDate)) {

						PoEdate = toDate;

					}

					Integer client_tenure = (int) ChronoUnit.MONTHS.between(PoSdate, PoEdate);

					Total_client_tenure[0] += client_tenure;

					long TillNowDays = ChronoUnit.DAYS.between(PoSdate, PoEdate);

					Total_client_days[0] += TillNowDays;

					Double salary = cl.getClientSalary();
					double perday = salary / 30;

					double Bill_at_client = TillNowDays * perday;

					Total_salary_from_client[0] = Total_salary_from_client[0] + Bill_at_client;

					cl.setTotalEarningAtclient(Math.ceil((Bill_at_client)));

//					System.out.println("EmpId" + employeeId);
//
//					System.out.println("Salary" + salary);
//
//					System.out.println("salary Per Day:" + perday);
//
//					System.out.println("TillNowDays" + TillNowDays);
//
//					System.out.println("pay at client" + Bill_at_client);

					cl.setClientTenure(TillNowDays);

					clientDetail.save(cl);

				}
			}

		}

	}

	public double allowancesOfEmployee(int employeeId, long TotalDays) {

		Optional<MasterEmployeeDetails> employee = masterEmployeeDetailsRepo.findById(employeeId);

		List<AtClientAllowances> records = allowancesOfEmployeeRepo.findsBymasterEmployeeDetails_Id(employeeId);

		double total = 0.0;

		if (!records.isEmpty()) {

			for (AtClientAllowances record : records) {

				if (record.getJoingBonus() != null) {

//					if (record.getJoiningBonusTenure() == null) {
//						record.setJoiningBonusTenure(0);
//					}

					Optional<ReleaseEmpDetails> rel = releaseEmpDetailsRepository
							.findBymasterEmployeeDetails_Id(employeeId);

					if (!rel.isEmpty()) {

						long ReleaseTenure = ChronoUnit.MONTHS.between(employee.get().getJoiningDate(),
								rel.get().getReleaseDate());

						if (ReleaseTenure >= record.getJoiningBonusTenure()) {
							total = total + record.getJoingBonus();
						}

					}

					else {

						long TotalTenure = ChronoUnit.MONTHS.between(employee.get().getJoiningDate(), LocalDate.now());

						if (TotalTenure >= record.getJoiningBonusTenure()) {
							total = total + record.getJoingBonus();
						}

					}

				}

				if (record.getShiftAllowance() != null) {

					double ShiftAllowancePerday = record.getShiftAllowance() / 30;

					total = total + TotalDays * ShiftAllowancePerday;

				}

				if (record.getSpecialAllowance() != null) {

					double SpecialAllowancePerday = record.getSpecialAllowance() / 30;

					total = total + TotalDays * SpecialAllowancePerday;

				}

				if (record.getDeputationAllowances() != null) {

					double DeptAllowancePerday = record.getDeputationAllowances() / 30;

					total = total + TotalDays * DeptAllowancePerday;

				}

				if (record.getExtraAllowance() != null) {

					double extraAllowancePerday = record.getExtraAllowance() / 30;

					total = total + TotalDays * extraAllowancePerday;

				}

			}

		}
		
	

		return total;
	}

}
