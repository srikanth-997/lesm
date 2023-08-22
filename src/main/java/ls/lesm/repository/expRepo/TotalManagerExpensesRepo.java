package ls.lesm.repository.expRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.TotalManagerExpenses;

public interface TotalManagerExpensesRepo extends JpaRepository<TotalManagerExpenses, Integer> {
	
	 @Query("FROM TotalManagerExpenses a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public Optional<TotalManagerExpenses> findByTotalIdFk(@Param("totalEmpExpenses")int id);

}
