package ls.lesm.repository.expRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.Travel;

public interface TravelRepo extends JpaRepository<Travel, Integer> {
	

	 @Query("FROM Travel a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public List<Travel> findByTotalIdFk(@Param("totalEmpExpenses")int id);


}
