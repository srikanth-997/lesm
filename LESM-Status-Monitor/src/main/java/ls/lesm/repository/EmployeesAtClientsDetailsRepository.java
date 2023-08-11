package ls.lesm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.payload.response.Response;
@Repository
@Transactional
public interface EmployeesAtClientsDetailsRepository extends JpaRepository<EmployeesAtClientsDetails, Integer> {
	@Query(nativeQuery = true, value="Select employees_at_clients_details.emp_at_client_id AS empAtClientId,employees_at_clients_details.desg_at_client AS desgAtClient, "
			+ "clients.clients_names AS clientsNames, master_emp_details.employee_id AS employeeId, master_emp_details.first_name AS firstName, master_emp_details.location AS location"
			+ " FROM ("
			+ "(employees_at_clients_details "
			+ "INNER JOIN clients ON employees_at_clients_details.clients_fk=clients.clients_id)"
			+ "INNER JOIN master_emp_details ON employees_at_clients_details.emp_id_fk=master_emp_details.emp_id)")	
	//@Query(nativeQuery = true)
	public List<Response> findDataResponseAll();
	
/*	@Query("SELECT e.empAtClientId "
		 + "FROM EmployeesAtClientsDetails e"
		 + "JOIN MasterEmployeeDetails m ON e.empAtClientId=m.emp_id_fk"
		 + "JOIN Clients c ON c.clients=c.clients_id"
		 + "WHERE c.clientsNames=c.clientsNames")
	public List<EmployeesAtClientsDetails> getDataaa();*/

	@Query("FROM EmployeesAtClientsDetails g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
    List<EmployeesAtClientsDetails>  findsBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	
	@Query(" FROM EmployeesAtClientsDetails g where g.masterEmployeeDetails.id = :masterEmployeeDetailsId")
	Optional<EmployeesAtClientsDetails>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	@Query("FROM EmployeesAtClientsDetails e WHERE e.clients.id = :clientId")
	Optional<EmployeesAtClientsDetails> findByClientId(@Param("clientId") int clientId);

	public List<EmployeesAtClientsDetails> findByMasterEmployeeDetails(MasterEmployeeDetails employe);
	
	Optional<EmployeesAtClientsDetails> findByPONumberIgnoreCase(String poNumber);
}
