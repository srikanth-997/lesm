package ls.lesm.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {

	@Query(" FROM Salary g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	Salary findByEmployeeById(@Param("masterEmployeeDetailsId")Integer id);
	

	@Query("FROM Salary g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	Optional<Salary>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	
	@Query("FROM Salary g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	List<Salary>  findsBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);


	List<Salary> findByUpdatedAtAndMasterEmployeeDetails(LocalDate sal, MasterEmployeeDetails employe);


	List<Salary> findByCreatedAtOrUpdatedAtAndMasterEmployeeDetails(LocalDate createdAt,LocalDate updatedAt, MasterEmployeeDetails employe);


	List<Salary> findByCreatedAtAndMasterEmployeeDetails(LocalDate maxDate, MasterEmployeeDetails employe);


	//List<Salary> findByUpdatedAtAndMasterEmployeeDetails(LocalDate sal, MasterEmployeeDetails employe);


	
	
	
	
	
	
}
