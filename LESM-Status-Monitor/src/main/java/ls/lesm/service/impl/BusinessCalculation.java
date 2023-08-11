package ls.lesm.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.exp.TotalFinanceExpenses;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.expRepo.TotalFinanceExpensesRepo;

@Service
public class BusinessCalculation {

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

	// double Total_expenses = 0l;// u

//    long Total_internal_tenure;
//    Double Total_internal_pay = 0.0;
//    Double totalFinanceExp = 0.0;

	public synchronized double Employee_cal(int employeeId, LocalDate fromDate, LocalDate toDate) {
		try {

			double profit_or_los = 0.0;
			long Total_internal_tenure = 0l;
			double Total_expenses = 0l;// u
			long tenure = 0l;
			Double Total_internal_pay = 0.0;
			Double totalFinanceExp = 0.0;
			double paid_till_now = 0l;
			double remaining_days = 0;
			double perday = 0;

			int inc = 0;

			Integer Total_client_tenure[] = { 0 };// u
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
					System.out.println("Joining date not present");
					return 0;

				}

			}

			MasterEmployeeDetails MasterEmployee = masterEmployeeDetailsRepo.findById(employeeId).get();

			List<Salary> salary = salaryRepo.findsBymasterEmployeeDetails_Id(employeeId);

			if (salary.isEmpty()) {

				return 0;

			}

			int salaryRecords = salary.size();

			if (salaryRecords == 1)// number_of_salary_records_of_single_employee==1
			{

				LocalDate Date1 = MasterEmployee.getJoiningDate();
				try {

					if (Date1 == null) {

						System.out.println("Joining date Error");
						return 0;

					}

				} catch (Exception e) {
					return 0;

				}

				LocalDate Date2 = null;

				if (MasterEmployee.getStatus() != EmployeeStatus.EXIT)

				{

					Date2 = LocalDate.now();

				}

				else {

					Date2 = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(employeeId).get()
							.getReleaseDate();

					if (Date2 == null) {
						System.out.println("Release date Error");
						return 0;

					}

					inc++;

					// Date2 = releaseDate;

				}

				// method

				if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

					if (Date1.isBefore(fromDate)) {

						Date1 = fromDate;

					} else {

						if (Date1.getDayOfMonth() != 1) {

							remaining_days = 31 - Date1.getDayOfMonth();

							System.out.println("\n\n\nRemaining days" + remaining_days + "\n\n\n");

							if (Date1.getMonthValue() == 12) {

								Date1 = LocalDate.of(Date1.getYear() + 1, 1, 1);
							} else {

								Date1 = LocalDate.of(Date1.getYear(), Date1.getMonthValue() + 1, 1);

							}

						}

					}
					if (Date2.isAfter(toDate)) {

						Date2 = toDate;

					}

					tenure = ChronoUnit.MONTHS.between(Date1, Date2);

					for (Salary s : salary) {

						///////////////////////////////////////////

						if (inc == 1) {

							int v1 = Date1.getDayOfMonth();
							int v2 = Date2.getDayOfMonth();
							int v3 = 0;

							if (v2 > v1) {

								v3 = v2 - v1;

							}

							else if (v1 > v2) {

								int tempV = 30 - v1;

								v3 = tempV + v2;

							}

							perday = s.getSalary() / 30;

							System.out.println(Date1.getDayOfMonth() + "  " + Date2.getDayOfMonth() + "  remainingdays="
									+ v3 + " remaining Amount=" + perday * v3);

							paid_till_now = tenure * s.getSalary() + perday * v3 + remaining_days * perday;

						}

						else {
							perday = s.getSalary() / 30;

							paid_till_now = tenure * s.getSalary() + remaining_days * perday;
						}

						System.out.println("tenure1=" + tenure + " salary=" + s.getSalary() + " paidTillNow="
								+ paid_till_now + " fromdate=" + fromDate + "   todate=" + toDate + "   startdate="
								+ Date1 + "   todate=" + Date2);

						Optional<InternalExpenses> inten = internalExpenseRepo
								.findBymasterEmployeeDetails_Id(employeeId);

						InternalExpenses expenses = null;

						if (!inten.isPresent()) {

							System.out.println("Am creating the record");
							// new InternalExpenses(MasterEmployee);
							internalExpenseRepo.save(new InternalExpenses(MasterEmployee));
							expenses = internalExpenseRepo.findBymasterEmployeeDetails_Id(employeeId).get();

						} else {
							expenses = inten.get();

						}

						expenses.setTotalSalPaidTillNow(Math.ceil(paid_till_now));

						clientCalculation(employeeId, fromDate, toDate, Total_client_tenure, Total_salary_from_client);

						long Bench_tenure = tenure - Total_client_tenure[0];

						expenses.setBenchTenure(Bench_tenure);

						List<TotalFinanceExpenses> totalFinanceExpenses = totalFinanceExpensesRepository
								.findBymasterEmployeeDetails_Id(employeeId);

						if (!totalFinanceExpenses.isEmpty()) {

							for (TotalFinanceExpenses tf : totalFinanceExpenses) {

								totalFinanceExp += tf.getTotal();

							}
						}

						profit_or_los = Total_salary_from_client[0] - (paid_till_now + totalFinanceExp);

						expenses.setTotalExpenses(Math.ceil(paid_till_now));
						expenses.setProfitOrLoss(Math.ceil(profit_or_los));

						internalExpenseRepo.save(expenses);

						System.out.println("  tenure=" + tenure + "    paidTillNow=" + paid_till_now + " fromdate="
								+ fromDate + "   todate=" + toDate + "   startdate1=" + Date1 + "    todate2="
								+ LocalDate.now() + "  profit======" + profit_or_los);

						break;

					}

				}

			} else {

				System.out.println("\n\n\nprofit" + profit_or_los);

				LocalDate Date1 = null;
				LocalDate Date2 = null;
				long i = 0;
				long remaining = 0;

				Double tempsal = 0.0;
				// ................................................................................

				for (Salary resolvenull : salary) {

					System.out.println("salary ud" + resolvenull.getUpdatedAt());
					if (resolvenull.getUpdatedAt() == null) {

						int index = salary.indexOf(resolvenull);

						resolvenull.setUpdatedAt(MasterEmployee.getJoiningDate());

						salary.set(index, resolvenull);

						System.out.println(salary.get(index));

					}

				}
				// ...................................................................................

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
					System.out.println("1");

					Date2 = record.getUpdatedAt();

					System.out.println(record.getUpdatedAt());
					if (Date2 == null) {
						System.err.print("Salary Updated date Error");

					}

					Double sal = record.getSalary();

					if (i == 0) {
						tempsal = sal;
						i++;
						continue;
					}

					if (Date1.isBefore(toDate) && Date2.isAfter(fromDate)) {

						if (Date1.isBefore(fromDate)) {

							Date1 = fromDate;

						} else {

							if (Date1.getDayOfMonth() != 1) {

								remaining_days = 31 - Date1.getDayOfMonth();

								if (Date1.getMonthValue() == 12) {

									Date1 = LocalDate.of(Date1.getYear() + 1, 1, 1);
								} else {

									Date1 = LocalDate.of(Date1.getYear(), Date1.getMonthValue() + 1, 1);

								}

							}
							remaining++;

						}

						if (Date2.isAfter(toDate)) {

							Date2 = toDate;

						} else {

							Date2 = LocalDate.of(Date2.getYear(), Date2.getMonthValue(), 1);

						}

						tenure = ChronoUnit.MONTHS.between(Date1, Date2);

						Total_internal_tenure += tenure;

						if (remaining == 1) {
							double p = tempsal / 30;

							paid_till_now = tenure * tempsal + remaining_days * p;
							remaining_days = 0;
							remaining--;

						} else {

							paid_till_now = tenure * tempsal;

						}

						Total_internal_pay += paid_till_now;

						System.out.println("  tenure=" + tenure + "    salary=" + tempsal + "    paidTillNow="
								+ paid_till_now + " fromdate=" + fromDate + "   todate=" + toDate + "    startdate="
								+ Date1 + " todate=" + Date2);

					}

					Date1 = Date2;
					tempsal = record.getSalary();
				}

				// tenure=Date1 to till date;

				if (MasterEmployee.getStatus() != EmployeeStatus.EXIT
						&& MasterEmployee.getStatus() != EmployeeStatus.ABSCONDED
						&& MasterEmployee.getStatus() != EmployeeStatus.TERMINATED)

				{

					Date2 = LocalDate.now();
					//

				}

				else {

					Date2 = releaseEmpDetailsRepository.findBymasterEmployeeDetails_Id(employeeId).get()
							.getReleaseDate();
					inc++;
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

					Total_internal_tenure += tenure;

					if (true) {

						int v1 = Date1.getDayOfMonth();
						int v2 = Date2.getDayOfMonth();
						int v3 = 0;

						if (v2 > v1) {

							v3 = v2 - v1;

						}

						else if (v1 > v2) {

							int tempV = 31 - v1;

							v3 = tempV + v2;

						}

						perday = tempsal / 30;

						paid_till_now = tenure * tempsal + v3 * perday;

					}

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

						clientCalculation(employeeId, fromDate, toDate, Total_client_tenure, Total_salary_from_client);

						long Bench_tenure = tenure - Total_client_tenure[0];

						expenses.setBenchTenure(Bench_tenure);

						List<TotalFinanceExpenses> totalFinanceExpenses = totalFinanceExpensesRepository
								.findBymasterEmployeeDetails_Id(employeeId);

						for (TotalFinanceExpenses tf : totalFinanceExpenses) {

							totalFinanceExp += tf.getTotal();

						}

						profit_or_los = Total_salary_from_client[0] - (Total_internal_pay + totalFinanceExp);

						expenses.setTotalExpenses(Math.ceil(Total_internal_pay));
						expenses.setProfitOrLoss(Math.ceil(profit_or_los));

					} catch (Exception e) {
						return 0;
					}

					internalExpenseRepo.save(expenses);

//					System.out.println("  tenure=" + tenure + "    salary=" + tempsal + "    paidTillNow="
//							+ paid_till_now + " fromdate=" + fromDate + "   todate=" + toDate + "   startdate1=" + Date1
//							+ "    todate2=" + LocalDate.now() + "  profit======" + profit_or_los);

				}

			}

			return profit_or_los;
		} catch (Exception e) {
			return 0;

		}
	}

	public void clientCalculation(int employeeId, LocalDate fromDate, LocalDate toDate, Integer Total_client_tenure[],
			double Total_salary_from_client[]) {
		System.out.println(employeeId + "---------------client");

//       Integer Total_client_tenure[] = 0;// u
//       double Total_salary_from_client = 0l;// u

		double remaining_days = 0;

		List<EmployeesAtClientsDetails> slidet = clientDetail.findsBymasterEmployeeDetails_Id(employeeId);// u
		System.out.println(employeeId + "---------------client" + slidet);
		if (!slidet.isEmpty())

		{

			for (EmployeesAtClientsDetails cl : slidet) {

				LocalDate PoSdate = cl.getPOSdate();

				LocalDate PoEdate = cl.getPOEdate();
				
				
				System.out.println("------------------------getClientJoiningDate"+cl.getClientJoiningDate());

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
					//

				}

				if (PoSdate.isBefore(toDate) && PoEdate.isAfter(fromDate)) {

					if (PoSdate.isBefore(fromDate)) {

						PoSdate = fromDate;

					} // .......................................................................

					else {

						if (PoSdate.getDayOfMonth() != 1 && PoSdate.getMonthValue() != PoEdate.getMonthValue()) {

							remaining_days = 30 - PoSdate.getDayOfMonth();

							// System.out.println("\n\n\nRemaining days" + remaining_days + "\n\n\n");

							if (PoSdate.getMonthValue() == 12) {

								PoSdate = LocalDate.of(PoSdate.getYear() + 1, 1, 1);
							} else {

								PoSdate = LocalDate.of(PoSdate.getYear(), PoSdate.getMonthValue() + 1, 1);

							}

						}

					}

					if (PoEdate.isAfter(toDate)) {

						PoEdate = toDate;

					}

					Period monthsAtClient = Period.between(PoSdate, PoEdate);

					Integer client_tenure = (monthsAtClient.getYears() * 12 + monthsAtClient.getMonths());

					System.out.println(client_tenure);

					double Bill_at_client = cl.getClientSalary() * client_tenure;

//					cl.setTotalEarningAtclient(Bill_at_client);

					// clientDetail.save(cl);

					Total_client_tenure[0] += client_tenure;

					Double salary = cl.getClientSalary();

					int v1 = PoSdate.getDayOfMonth();
					int v2 = PoEdate.getDayOfMonth();
					int v3 = 0;

					if (v2 > v1) {

						v3 = v2 - v1 + 1;

					}

					else if (v1 > v2) {

						int tempV = 30 - v1;

						v3 = tempV + v2 + 1;

					}

					if (PoEdate.getDayOfMonth() == 1) {

						v3 = 1;
					}

					double perday = salary / 30;
//					System.out.println("\n\n...........................client Details.........................");
//
//					System.out.println("\nPoSdate=" + PoSdate + "  PoEdate=" + PoEdate + "  strating remaining days="
//							+ remaining_days + " Ending Remaining days=" + v3);

					Total_salary_from_client[0] = Total_salary_from_client[0] + (cl.getClientSalary() * client_tenure)
							+ perday * v3 + remaining_days * perday;
					cl.setTotalEarningAtclient(
							Math.ceil((cl.getClientSalary() * client_tenure) + perday * v3 + remaining_days * perday));

					clientDetail.save(cl);

				}
			}
		}

	}

}