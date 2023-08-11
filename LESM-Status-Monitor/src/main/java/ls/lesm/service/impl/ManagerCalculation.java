package ls.lesm.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Sub_Profit;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;

@Service
@Transactional
public class ManagerCalculation {
	Long total = 0l;


	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	InternalExpensesRepository internalExpensesrepo;

	@Autowired
	Sub_ProfitRepository sub_ProfitRepository;

	@Autowired
	LeadCalculation lc;
	
	  @Autowired
	    CalculationsUptoDate bc;

	public synchronized Double manager_cal(int ManagerEmployeeId, LocalDate fromDate, LocalDate toDate) {

		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository
				.findBymasterEmployeeDetails_Id(ManagerEmployeeId);

		Double profit_or_loss = 0.0;
		Double sub_profit = 0.0;

		if (!ls.isEmpty()) {

			for (MasterEmployeeDetails Employeeid : ls) {

				System.out.println(Employeeid);

//			Optional<MasterEmployeeDetails> id = masterEmployeeDetailsRepository.findById(Employeeid.getEmpId());
//
//			MasterEmployeeDetails epm = null;
//
//			if (id.isPresent()) {
//				epm = id.get();
//			}

				// int a = Employeeid .getEmpId();

				

				profit_or_loss = Math.ceil (lc.lead_cal(Employeeid.getEmpId(), fromDate, toDate));

			
				sub_profit += profit_or_loss;

			}

		}

		Optional<InternalExpenses> i = internalExpensesrepo.findBymasterEmployeeDetails_Id(ManagerEmployeeId);

		InternalExpenses o = null;
		if (i.isPresent()) {
			o = i.get();

		} else {
			o = internalExpensesrepo
					.save(new InternalExpenses(masterEmployeeDetailsRepository.findById(ManagerEmployeeId).get()));

		}

		Double Total_profit_or_loss = sub_profit + bc.lesmCalculations(ManagerEmployeeId, fromDate, toDate);

		o.setProfitOrLoss(Math.ceil(Total_profit_or_loss));

		//

		Optional<MasterEmployeeDetails> me = masterEmployeeDetailsRepository.findById(ManagerEmployeeId);
		MasterEmployeeDetails med = me.get();

		Optional<Sub_Profit> s_p = sub_ProfitRepository.findBymasterEmployeeDetails_Id(ManagerEmployeeId);

		if (s_p.isPresent()) {
			Sub_Profit s = s_p.get();

			s.setSubprofit(Math.ceil(sub_profit));

			sub_ProfitRepository.save(s);

		}

		else {

			Sub_Profit sp = new Sub_Profit(Math.ceil(sub_profit), med);

		

			sub_ProfitRepository.save(sp);

		}
		return Total_profit_or_loss;

		// return (Double) (sub_profit - bc.Employee_cal( ManagerEmployeeId));

	}
}
