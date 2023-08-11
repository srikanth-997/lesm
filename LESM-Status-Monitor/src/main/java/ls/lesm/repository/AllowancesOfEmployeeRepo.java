package ls.lesm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ls.lesm.model.AtClientAllowances;
import ls.lesm.model.MasterEmployeeDetails;

@Repository
public interface AllowancesOfEmployeeRepo extends JpaRepository<AtClientAllowances,Integer> {

	@Query("FROM AtClientAllowances g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	List<AtClientAllowances> findsBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId") Integer id);

	List<AtClientAllowances> findByMasterEmployeeDetails(MasterEmployeeDetails employe);

}
