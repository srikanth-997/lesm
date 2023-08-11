package ls.lesm.repository.expRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.Accommodation;
import ls.lesm.model.exp.Allowance;

public interface AllowanceRepo extends JpaRepository<Allowance, Integer> {


	 @Query("FROM Allowance a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public Allowance findByTotalIdFk(@Param("totalEmpExpenses")int id);

}
