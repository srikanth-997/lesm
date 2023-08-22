package ls.lesm.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.OnsiteBillExpenses;

public interface OnsiteBillExpensesRepository extends JpaRepository<OnsiteBillExpenses, Integer> {

	@Query("FROM OnsiteBillExpenses g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	List<OnsiteBillExpenses>  findByMasterEmployeeDetails_Id( @Param("masterEmployeeDetailsId") int  id);
}
