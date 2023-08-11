package ls.lesm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ReleaseEmpDetails;

public interface ReleaseEmpDetailsRepository  extends JpaRepository<ReleaseEmpDetails,Integer> {
	
	
	
	@Query("FROM ReleaseEmpDetails g where g.masterEmployeeDetailsId.id = :masterEmployeeDetailsId")
    Optional<ReleaseEmpDetails>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	  @Query("FROM ReleaseEmpDetails g where g.masterEmployeeDetailssupervisor.id = :masterEmployeeDetailssupervisor")
	    List<ReleaseEmpDetails>  findsBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailssupervisor")Integer id);

	Optional<ReleaseEmpDetails> findByMasterEmployeeDetailsId(MasterEmployeeDetails employe);

	Optional<ReleaseEmpDetails> MasterEmployeeDetailsId(MasterEmployeeDetails employe);

	

	//Optional<ReleaseEmpDetails> findByMasterEmployeeDetailsId(MasterEmployeeDetails employe);
	

}
