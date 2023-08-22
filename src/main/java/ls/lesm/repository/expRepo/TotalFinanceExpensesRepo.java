package ls.lesm.repository.expRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.TotalFinanceExpenses;
import ls.lesm.model.exp.TotalManagerExpenses;

public interface TotalFinanceExpensesRepo extends JpaRepository<TotalFinanceExpenses, Integer> {
	
	 @Query("FROM TotalFinanceExpenses a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public Optional<TotalFinanceExpenses> findByTotalIdFk(@Param("totalEmpExpenses")int id);
	 
	 
	 @Query("FROM TotalFinanceExpenses g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	 List<TotalFinanceExpenses>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	 
	 
	 
	 

}
