package ls.lesm.repository.expRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.Food;

public interface FoodRepo extends JpaRepository<Food, Integer> {
	

	 @Query("FROM Food a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public List<Food> findByTotalIdFk(@Param("totalEmpExpenses")int id);


}
