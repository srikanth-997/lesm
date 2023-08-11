package ls.lesm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.Address;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.payload.response.ForeignkeyResponse;

public interface InternalExpensesRepository extends JpaRepository<InternalExpenses, Integer> {


	@Query(" FROM InternalExpenses g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	Optional<InternalExpenses>  findByEmployeeById(@Param("masterEmployeeDetailsId")Integer id);
	
	@Query(nativeQuery = true, value="SELECT address.emp_id_fk AS fKey FROM address WHERE address.emp_id_fk= ?1 ")
	   public List<ForeignkeyResponse> getKey(int id);
	
	@Query("FROM InternalExpenses g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	Optional<InternalExpenses>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	@Query("FROM InternalExpenses g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
   List<InternalExpenses>  findsBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	 @Query("FROM InternalExpenses a where a.masterEmployeeDetails.id= :masterEmployeeDetailsId")
	   public Optional<InternalExpenses> findByEmpIdFk(@Param("masterEmployeeDetailsId")int empId);

	List<InternalExpenses> findByMasterEmployeeDetails(MasterEmployeeDetails employe);
	
}
