package ls.lesm.payload.response;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import ls.lesm.model.exp.Accommodation;
import ls.lesm.model.exp.Allowance;
import ls.lesm.model.exp.Food;
import ls.lesm.model.exp.OtherExpenses;
import ls.lesm.model.exp.TotalEmpExpenses;
import ls.lesm.model.exp.Travel;

@Setter
@Getter
//UMER
public class AllExpensesResponse {
	

		private Allowance allowance;

		private List<Food> food;

		private List<Travel> travel;
	
		private List<Accommodation> accommodation;

	
		private List<OtherExpenses> otherExpenses;
		
		private Optional<TotalEmpExpenses> totalEmpExpenses;

}
