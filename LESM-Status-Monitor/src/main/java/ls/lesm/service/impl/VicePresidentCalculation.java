package ls.lesm.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Sub_Profit;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;

@Service
public class VicePresidentCalculation {
	
	
Long total=0l;
	
	@Autowired
	BusinessCalculation bc;
	
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	GeneralManagerCalculation generalManagerCalculation;
	
	@Autowired
    InternalExpensesRepository internalExpensesrepo;

    @Autowired
    Sub_ProfitRepository sub_ProfitRepository;
	
	
	
	//public Double countryHeadCal(int CountryHead,LocalDate fromDate, LocalDate toDate)
    public synchronized Double vicePresident(int vicepresidentval,LocalDate fromDate, LocalDate toDate)
	{

		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(vicepresidentval);

		Double profit_or_loss=0.0;
		Double sub_profit=0.0;
		
		if(!ls.isEmpty())
		{
		
		
		for (MasterEmployeeDetails Employeeid : ls) {

			System.out.println(Employeeid);
//
//			Optional<MasterEmployeeDetails> id = masterEmployeeDetailsRepository.findById(Employeeid.getEmpId());
//
//			MasterEmployeeDetails epm = null;
//
//			if (id.isPresent()) {
//				epm = id.get();
//			}

			int a = Employeeid.getEmpId();
			
			 System.out.println("\nd an entering VP...............\\n ");

			profit_or_loss = Math.ceil(generalManagerCalculation.generalManagercal(a,fromDate,toDate));
			
			  System.out.println("\nd an leaving VP...............\\n");
			sub_profit += profit_or_loss;

		}
		}
		
		Optional<InternalExpenses> i = internalExpensesrepo.findBymasterEmployeeDetails_Id(vicepresidentval);

		InternalExpenses o=null;
		 if(i.isPresent())
		 {
			 o = i.get();
			 
			 
			 
		 }
		 else
		 {
			o=internalExpensesrepo.save(new InternalExpenses(masterEmployeeDetailsRepository.findById(vicepresidentval).get()));
			 
		 }

        Double Total_profit_or_loss = sub_profit + bc.Employee_cal(vicepresidentval,fromDate,toDate);

        o.setProfitOrLoss(Math.ceil(Total_profit_or_loss));

        //

        Optional<MasterEmployeeDetails> me = masterEmployeeDetailsRepository.findById(vicepresidentval);
        MasterEmployeeDetails med = me.get();


        Optional<Sub_Profit> s_p=sub_ProfitRepository.findBymasterEmployeeDetails_Id(vicepresidentval);

        if(s_p.isPresent())
        {
            Sub_Profit s=s_p.get();

            s.setSubprofit(Math.ceil(sub_profit));

            sub_ProfitRepository.save(s);

        }

        else
        {

        Sub_Profit sp = new Sub_Profit(Math.ceil(sub_profit), med);

        System.out.println(sp);

        sub_ProfitRepository.save(sp);

        }
        return Total_profit_or_loss;
		
		//return (Double)(sub_profit - bc.Employee_cal(CountryHead));


	}
}

	


