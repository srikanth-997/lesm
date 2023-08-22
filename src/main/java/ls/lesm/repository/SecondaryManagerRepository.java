package ls.lesm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.SecondaryManager;

public interface SecondaryManagerRepository extends JpaRepository<SecondaryManager, Integer> {
//	@Query("FROM SecondaryManager g where g.employee.id = :employee")
//	SecondaryManager findByEmployees(@Param("employee") MasterEmployeeDetails id);
	Optional<SecondaryManager> findByEmployee(MasterEmployeeDetails masterEmployeeDetails);


	List<SecondaryManager> findBySecondaryManager(MasterEmployeeDetails loggedInEmployee);

	Optional<SecondaryManager> findByEmployeeAndSecondaryManager(MasterEmployeeDetails masterEmployeeDetails,
			MasterEmployeeDetails masterEmployeeDetails2);
	
	@Query("FROM SecondaryManager As g where g.employee.empId=:emp")
	 SecondaryManager findByEmployee(@Param("emp")  int empId);
	
	
	
	
	
	@Query("FROM SecondaryManager g where g.secondaryManager.id = :secondaryManager")
	List<SecondaryManager> findBysecondaryManager(@Param("secondaryManager") Integer id);

	
}
