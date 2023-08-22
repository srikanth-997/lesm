package ls.lesm.repository.expRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.OtherExpenses;

public interface OtherExpensesRepo extends JpaRepository<OtherExpenses, Integer> {
	

	 @Query("FROM OtherExpenses a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public List<OtherExpenses> findByTotalIdFk(@Param("totalEmpExpenses")int id);


}
