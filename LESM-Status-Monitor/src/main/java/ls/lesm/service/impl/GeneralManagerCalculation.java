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
public class GeneralManagerCalculation {

	Long total=0l;
	
	
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	ManagerCalculation managerCalculation;
	
	@Autowired
    InternalExpensesRepository internalExpensesrepo;

    @Autowired
    Sub_ProfitRepository sub_ProfitRepository;
	
    @Autowired
    CalculationsUptoDate bc;
	
	public synchronized Double generalManagercal(int GeneralManagerEmployeeId,LocalDate fromDate, LocalDate toDate)
	{

		List<MasterEmployeeDetails> ls = masterEmployeeDetailsRepository.findBymasterEmployeeDetails_Id(GeneralManagerEmployeeId);

		Double profit_or_loss=0.0;
		Double sub_profit=0.0;
		
		if(!ls.isEmpty())
		{
		for (MasterEmployeeDetails Employeeid : ls) {

			System.out.println(Employeeid);

//			Optional<MasterEmployeeDetails> id = masterEmployeeDetailsRepository.findById(Employeeid.getEmpId());
//
//			MasterEmployeeDetails epm = null;
//
//			if (id.isPresent()) {
//				epm = id.get();
//			}

			int a = Employeeid.getEmpId();
			
			 System.out.println("\nd an entering GM ...............\\n");

			profit_or_loss = (Double)managerCalculation.manager_cal(a,fromDate,toDate);
			
			  System.out.println("\nd an leaving GM ...............\\n");
			sub_profit += profit_or_loss;

		}
		}
		
		Optional<InternalExpenses> i = internalExpensesrepo.findBymasterEmployeeDetails_Id(GeneralManagerEmployeeId);

		InternalExpenses o=null;
		 if(i.isPresent())
		 {
			 o = i.get();
			 
			 
			 
		 }
		 else
		 {
			o=internalExpensesrepo.save(new InternalExpenses(masterEmployeeDetailsRepository.findById(GeneralManagerEmployeeId).get()));
			 
		 }

        Double Total_profit_or_loss = sub_profit + bc.lesmCalculations(GeneralManagerEmployeeId,fromDate,toDate);

        o.setProfitOrLoss(Math.ceil(Total_profit_or_loss));

        //

        Optional<MasterEmployeeDetails> me = masterEmployeeDetailsRepository.findById(GeneralManagerEmployeeId);
        MasterEmployeeDetails med = me.get();


        Optional<Sub_Profit> s_p=sub_ProfitRepository.findBymasterEmployeeDetails_Id(GeneralManagerEmployeeId);

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
		//return (Double)(sub_profit - bc.Employee_cal(GeneralManagerEmployeeId));


	}
}
