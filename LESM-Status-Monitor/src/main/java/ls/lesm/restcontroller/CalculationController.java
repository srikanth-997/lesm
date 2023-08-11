package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.DateRange;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Sub_Profit;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;
import ls.lesm.service.impl.BusinessCalculation;
import ls.lesm.service.impl.CalculationsUptoDate;
import ls.lesm.service.impl.CountryHeadCalculation;
import ls.lesm.service.impl.DefaultCalculationsServiceImpl;
import ls.lesm.service.impl.GeneralManagerCalculation;
import ls.lesm.service.impl.LeadCalculation;
import ls.lesm.service.impl.ManagerCalculation;
import ls.lesm.service.impl.ManagingDirectorCalculation;
import ls.lesm.service.impl.VicePresidentCalculation;

@RestController
@RequestMapping("/Total")
@CrossOrigin("*")
public class CalculationController {

	@Autowired
	BusinessCalculation businessCalculation;

	@Autowired
	LeadCalculation leadCalculation;

	@Autowired
	ManagerCalculation managerCalculation;

	@Autowired
	GeneralManagerCalculation generalManagerCalculation;

	@Autowired
	CountryHeadCalculation countryHeadCalculation;

	@Autowired
	ManagingDirectorCalculation managingDirectorCalculation;

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	Sub_ProfitRepository sub_ProfitRepository;

	@Autowired
	VicePresidentCalculation vicePresidentCalculation;

	@Autowired
	DesignationsRepository designationsRepository;

	@Autowired
	CalculationsUptoDate calculationsUptoDate;

	@Autowired
	DefaultCalculationsServiceImpl defaultCalculationsServiceImpl;

	@Autowired
	InternalExpensesRepository internalExpensesrepo;

	@PostMapping("/consultant/{id}")
	public Double s(@PathVariable int id, @RequestBody DateRange dateRange)

	{

		Double profit_or_loss = calculationsUptoDate.lesmCalculations(id, dateRange.getFromDate(),
				dateRange.getToDate());
		// Double profit_or_loss = businessCalculation.Employee_cal(id,
		// dateRange.getFromDate(), dateRange.getToDate());

		return profit_or_loss;

	}

//	
//	@PostMapping("/lead")
//	public Double lead( Principal principal)
//	{
//		String emp1=principal.getName();
//		MasterEmployeeDetails emp2=this.masterEmployeeDetailsRepository.findByLancesoft(emp1);
//		int id=emp2.getEmpId();
//		Double profit_or_loss=leadCalculation.lead_cal(id);
//	       Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);
//
//	        Sub_Profit spt = sp.get();
//
//	        Double subprofit = spt.getSubprofit();
//
//	        return (subprofit);
//		
//		//return profit_or_loss;
//		
//	}

	@PostMapping("/lead")
	public ResponseEntity<Double> lead(Principal principal, @RequestBody DateRange dateRange) {

		try {

			String emp1 = principal.getName();
			MasterEmployeeDetails emp2 = this.masterEmployeeDetailsRepository.findByLancesoft(emp1);
			int id = emp2.getEmpId();
			leadCalculation.lead_cal(id, dateRange.getFromDate(), dateRange.getToDate());
			Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

			Sub_Profit spt = sp.get();

			Double subprofit = spt.getSubprofit();

			// return (subprofit);
			return new ResponseEntity<Double>((double) (int) (double) subprofit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Double>(1.0, HttpStatus.OK);
		}

		// return profit_or_loss;

	}

	@PostMapping("/managerCalculation")
	public ResponseEntity<Double> managerCalculation(Principal principal, @RequestBody DateRange dateRange) {
		try {

			String loggedIn = principal.getName();
			MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
			int id = employee.getEmpId();
			managerCalculation.manager_cal(id, dateRange.getFromDate(), dateRange.getToDate());

			Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

			Sub_Profit spt = sp.get();

			Double subprofit = spt.getSubprofit();

			return new ResponseEntity<Double>((double) (int) (double) subprofit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Double>(1.0, HttpStatus.OK);
		}

		// return profit_or_loss;

	}

	@PostMapping("/generalManagerCalculation")
	public ResponseEntity<Double> generalManagerCalculationManagerCalculation(Principal principal,
			@RequestBody DateRange dateRange) {
		try {
			String loggedIn = principal.getName();
			MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
			int id = employee.getEmpId();
			generalManagerCalculation.generalManagercal(id, dateRange.getFromDate(), dateRange.getToDate());

			Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

			Sub_Profit spt = sp.get();

			Double subprofit = spt.getSubprofit();

			return new ResponseEntity<Double>((double) (int) (double) subprofit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Double>(1.0, HttpStatus.OK);
		}

		// return profit_or_loss;

	}

//	@PostMapping("/countryHeadCalculation/{id}")
//	public ResponseEntity<Double>  countryHeadCalculation(@PathVariable int id, @RequestBody DateRange dateRange) {
////		String loggedIn = principal.getName();
////		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
////		int id = employee.getEmpId();
//		countryHeadCalculation.countryHeadCal(id, dateRange.getFromDate(), dateRange.getToDate());
//
//		Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);
//
//		Sub_Profit spt = sp.get();
//
//		Double subprofit = spt.getSubprofit();
//
//		return  new ResponseEntity<Double>(subprofit, HttpStatus.OK);
//
//		// return profit_or_loss;
//
//	}

	@PostMapping("/countryHeadCalculation")
	public ResponseEntity<Double> countryHeadCalculation(Principal principal, @RequestBody DateRange dateRange) {

		try {

			String loggedIn = principal.getName();
			MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
			int id = employee.getEmpId();
			countryHeadCalculation.countryHeadCal(id, dateRange.getFromDate(), dateRange.getToDate());

			Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

			Sub_Profit spt = sp.get();

			Double subprofit = spt.getSubprofit();

			return new ResponseEntity<Double>((double) (int) (double) subprofit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Double>(1.0, HttpStatus.OK);
		}
		// return profit_or_loss;

	}

	@PostMapping("/managingDirectorCalculation")
	public ResponseEntity<Double> managingDirectorCalculation(Principal principal, @RequestBody DateRange dateRange) {
		try {
			String loggedIn = principal.getName();
			MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
			int id = employee.getEmpId();
			managingDirectorCalculation.managingDirectorCal(id, dateRange.getFromDate(), dateRange.getToDate());
			Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

			Sub_Profit spt = sp.get();

			Double subprofit = spt.getSubprofit();

			return new ResponseEntity<Double>((double) (int) (double) subprofit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Double>(1.0, HttpStatus.OK);
		}

	}

//	@GetMapping("/managingDirectorCalculation/{id}")
//	public Double managingDirectorCalculation(@PathVariable int id, @RequestBody DateRange dateRange) {
////		String loggedIn = principal.getName();
////		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
////		int id = employee.getEmpId();
//		managingDirectorCalculation.managingDirectorCal(id, dateRange.getFromDate(), dateRange.getToDate());
//		Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);
//
//		Sub_Profit spt = sp.get();
//
//		Double subprofit = spt.getSubprofit();
//
//		return (subprofit);
//	}

	// add ons
	@PostMapping("/vicePresident")
	public ResponseEntity<Double> vicePresident(Principal principal, @RequestBody DateRange dateRange) {
		try {
			String loggedIn = principal.getName();
			MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(loggedIn);
			int id = employee.getEmpId();
			vicePresidentCalculation.vicePresident(id, dateRange.getFromDate(), dateRange.getToDate());

			Optional<Sub_Profit> sp = sub_ProfitRepository.findBymasterEmployeeDetails_Id(id);

			Sub_Profit spt = sp.get();

			Double subprofit = spt.getSubprofit();

			return new ResponseEntity<Double>((double) (int) (double) subprofit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Double>(1.0, HttpStatus.OK);
		}

	}

	@GetMapping("/SuperAdminDefaultCalculator")

	public void superAdminDefaultCalculation() {

		try {
			defaultCalculationsServiceImpl.defaultCalculationCall();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@PostMapping("/RootHierarchyCalculation/{id}")

	public void RootHierarchCalculation(@PathVariable("id") String lancesoftId) {

		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(lancesoftId);
		if (employee == null)

			return;

		int id = employee.getEmpId();
		Double sub_profit = 0.0;
		Double previous_salary = 0.0;

		
		Optional<InternalExpenses> if_update_del_per = internalExpensesrepo
				.findBymasterEmployeeDetails_Id(employee.getEmpId());

		InternalExpenses upSal = null;

		if (if_update_del_per.isPresent()) {
			upSal = if_update_del_per.get();

			previous_salary = upSal.getProfitOrLoss();

		}

		Double pay_of_new_Employee = calculationsUptoDate.lesmCalculations(id, null, null);

		MasterEmployeeDetails supervisor = employee.getSupervisor();

		for (; supervisor != null; supervisor = supervisor.getSupervisor())

		{

			try {

				MasterEmployeeDetails supervisorLoopCheck = supervisor.getSupervisor().getSupervisor();

				if (supervisor == supervisorLoopCheck) {

					break;

				}

			}

			catch (Exception e)

			{
				// System.out.println("No Issue");
			}

			Optional<Sub_Profit> s_p = sub_ProfitRepository.findBymasterEmployeeDetails_Id(supervisor.getEmpId());

			if (s_p.isPresent()) {
				Sub_Profit s = s_p.get();

				sub_profit = s.getSubprofit() + pay_of_new_Employee - previous_salary;

				s.setSubprofit(Math.ceil(sub_profit));

				sub_ProfitRepository.save(s);

			}

			else {
				Optional<MasterEmployeeDetails> me = masterEmployeeDetailsRepository.findById(supervisor.getEmpId());
				MasterEmployeeDetails med = me.get();

				Sub_Profit sp = new Sub_Profit(Math.ceil(pay_of_new_Employee), med);

				sub_profit = pay_of_new_Employee - previous_salary;

				sub_ProfitRepository.save(sp);

			}

			// update Profit/Loss

			Optional<InternalExpenses> i = internalExpensesrepo.findBymasterEmployeeDetails_Id(supervisor.getEmpId());

			InternalExpenses o = null;
			if (i.isPresent()) {
				o = i.get();

			} else {
				o = internalExpensesrepo.save(
						new InternalExpenses(masterEmployeeDetailsRepository.findById(supervisor.getEmpId()).get()));

				o.setProfitOrLoss(0.0);

			}

			Double Total_profit_or_loss = o.getProfitOrLoss() + pay_of_new_Employee - previous_salary;

			o.setGPM_INR(Math.ceil(Total_profit_or_loss));
			o.setGPM_USD(Math.ceil(Total_profit_or_loss / 74));

			o.setProfitOrLoss(Math.ceil(Total_profit_or_loss));

			//

			internalExpensesrepo.save(o);

		}

	}

}