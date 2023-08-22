package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.EmployeePhoto;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.SecondaryManager;

public interface EmployeePhotoRepo extends JpaRepository<EmployeePhoto, Integer>{

	Optional<EmployeePhoto> findByMasterEmployeeDetails(MasterEmployeeDetails employee);


	//@Query("FROM EmployeePhoto As g where g.masterEmployeeDetails.empId=:emp")
	@Query("select p from EmployeePhoto p where p.masterEmployeeDetails.empId=:emp")
	EmployeePhoto findByEmployee(@Param("emp")  int empId);

	

}
