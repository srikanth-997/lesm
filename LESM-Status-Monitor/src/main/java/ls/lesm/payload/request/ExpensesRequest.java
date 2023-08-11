package ls.lesm.payload.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.exp.Accommodation;
import ls.lesm.model.exp.Allowance;
import ls.lesm.model.exp.ExpenseType;
import ls.lesm.model.exp.Food;
import ls.lesm.model.exp.OtherExpenses;
import ls.lesm.model.exp.Travel;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//UMER
public class ExpensesRequest {
	 @Nullable
	private Allowance allowance;
	 @Nullable
	private List<Food> food;
	 @Nullable
	private List<Travel> travel;
	 @Nullable
	private Set<Accommodation> accommodation;

	@Column(nullable = true)
    @Nullable
	private List<OtherExpenses> otherExpenses;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	
	

	
	
	

	
    private ExpenseType expenseType;
    

}
