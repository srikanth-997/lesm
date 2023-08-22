package ls.lesm.repository.expRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.exp.Accommodation;

public interface AccomodationRepo extends JpaRepository<Accommodation, Integer> {
	
	 @Query("FROM Accommodation a where a.totalEmpExpenses.id= :totalEmpExpenses")
	 public List<Accommodation> findByTotalIdFk(@Param("totalEmpExpenses")int id);

}
