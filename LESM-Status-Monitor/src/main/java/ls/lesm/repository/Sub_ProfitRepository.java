package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.Sub_Profit;

public interface Sub_ProfitRepository extends JpaRepository<Sub_Profit, Integer> {
	
	  @Query("FROM Sub_Profit g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	    Optional< Sub_Profit>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);

}
