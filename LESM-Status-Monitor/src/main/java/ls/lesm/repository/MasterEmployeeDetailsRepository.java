
package ls.lesm.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.SubDepartments;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.payload.response.AllEmpCardDetails;
import ls.lesm.payload.response.ClientEmpDropDownResponse;
import ls.lesm.payload.response.EmployeeDetailsResponse;
import ls.lesm.payload.response.EmployeeUnderHRDropDownResponse;
import ls.lesm.payload.response.ForeignkeyResponse;
import ls.lesm.payload.response.InitialExpenseDetailResponse;
import ls.lesm.payload.response.SupervisorDropDown;

@Repository
public interface MasterEmployeeDetailsRepository extends JpaRepository<MasterEmployeeDetails, Integer> {

	@Query(nativeQuery = true, value = " SELECT master_emp_details.emp_id AS empId, master_emp_details.employee_id AS employeeId, master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName,"
			+ "master_emp_details.dob AS dob, master_emp_details.location AS location, master_emp_details.email AS email, master_emp_details.gender AS gender, "
			+ "master_emp_details.joining_date, master_emp_details.phone_no, master_emp_details.status, departments.depart AS department, "
			+ "designations.desg_names AS designation, employee_type.type_name AS employeeType, sub_departments.sub_department_names AS subDepartName"
			+ " FROM( ((" + "(master_emp_details" + " LEFT JOIN departments"
			+ " ON master_emp_details.department_fk=departments.depart_id) " + " LEFT JOIN designations "
			+ " ON master_emp_details.desg_fk=designations.desg_id ) " + " LEFT JOIN employee_type"
			+ " ON master_emp_details.emp_type_fk=employee_type.emp_type_id)" + " LEFT JOIN sub_departments"
			+ " ON master_emp_details.sub_depart_fk=sub_departments.sub_depart_id);")
	public List<EmployeeDetailsResponse> getAllEmpDetails();

	@Query("FROM MasterEmployeeDetails g where g.supervisor.id = :supervisor")
	List<MasterEmployeeDetails> findBymasterEmployeeDetails_Id(@Param("supervisor") Integer id);

	@Query("FROM MasterEmployeeDetails g where g.designations.id = :designations")
	List<MasterEmployeeDetails> findsBymasterEmployeeDetails_Id(@Param("designations") Integer id);
	
	
	
	// public MasterEmployeeDetails findByLancesoft(String id);
	public MasterEmployeeDetails findByLancesoft(String ids);
	// public Optional<MasterEmployeeDetails> findByLancesoft(String id);

//	 @Query(nativeQuery = true, value="SELECT master_emp_details.supervisor_fk AS fKey FROM master_emp_details WHERE master_emp_details.supervisor_fkk= ?1 ")
//	   public List<ForeignkeyResponse> getKey(int id);

	@Query(nativeQuery = true, value = "SELECT master_emp_details.supervisor_fk AS fKey,master_emp_details.emp_id AS id FROM master_emp_details WHERE master_emp_details.supervisor_fk= ?1 ")
	public ForeignkeyResponse getKey(Integer id);

	@Query(nativeQuery = true, value = " SELECT master_emp_details.emp_id AS empId, master_emp_details.employee_id AS employeeId, master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName, master_emp_details.dob AS dob, master_emp_details.location AS location, master_emp_details.email AS email, master_emp_details.gender AS gender, master_emp_details.joining_date, master_emp_details.phone_no, master_emp_details.status, departments.depart, designations.desg_names AS designation, employee_type.type_name AS employeeType, sub_departments.sub_department_names AS subDepartName FROM((((master_emp_details LEFT JOIN departments ON master_emp_details.department_fk=departments.depart_id) LEFT JOIN designations ON master_emp_details.desg_fk=designations.desg_id )LEFT JOIN employee_type ON master_emp_details.emp_type_fk=employee_type.emp_type_id) LEFT JOIN sub_departments ON master_emp_details.sub_depart_fk=sub_departments.sub_depart_id) WHERE master_emp_details.supervisor_fk= ?1")
	public List<EmployeeDetailsResponse> getEmpDetails(@Param("id") int id);

//@Query("SELECT m,i,s,e "
//		+ "FROM MasterEmployeeDetails m, InteranExpenses i, salary s, EmployeesAtClientsDetails e "
//		+ "m.empId= :id AND i.supervisor m.empId AND s.supervisor=m.empId AND s.supervisor=m.empId AND e.supervisor=m.empId")
//public List<Object[]> getD(Integer id);

	@Query(nativeQuery = true, value = " SELECT master_emp_details.emp_id AS empId, master_emp_details.phone_no AS phoneNo,master_emp_details.status AS Status, master_emp_details.joining_date AS joiningDate,  master_emp_details.vertical AS vertical, master_emp_details.employee_id AS employeeId, master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName,"
			+ "master_emp_details.dob AS dob, master_emp_details.location AS location, master_emp_details.email AS email, master_emp_details.gender AS gender, "
			+ "master_emp_details.joining_date, master_emp_details.phone_no, master_emp_details.status, departments.depart AS department, "
			+ "designations.desg_names AS designation, employee_type.type_name AS employeeType, sub_departments.sub_department_names AS subDepartName"
			+ " FROM( ((" + "(master_emp_details" + " LEFT JOIN departments"
			+ " ON master_emp_details.department_fk=departments.depart_id) " + " LEFT JOIN designations "
			+ " ON master_emp_details.desg_fk=designations.desg_id ) " + " LEFT JOIN employee_type"
			+ " ON master_emp_details.emp_type_fk=employee_type.emp_type_id)" + " LEFT JOIN sub_departments"
			+ " ON master_emp_details.sub_depart_fk=sub_departments.sub_depart_id) "
			+ "WHERE master_emp_details.emp_id= ?1 ")
	public EmployeeDetailsResponse getEmpDetailsById(@Param("id") int id);

	@Query(nativeQuery = true, value = "SELECT master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName, master_emp_details.employee_id AS lancesoftId, master_emp_details.desg_fk AS desgId From master_emp_details WHERE master_emp_details.supervisor_fk= ?1 OR master_emp_details.supervisor_fk IS NULL ")
	public List<EmployeeUnderHRDropDownResponse> getEmpUnderHrDropDown(@Param("id") int id);

	@Query(nativeQuery = true, value = "SELECT master_emp_details.employee_id As lancesoftId,master_emp_details.emp_id As empId, master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName FROM master_emp_details where master_emp_details.supervisor_fk= ?1 ")
	public List<ClientEmpDropDownResponse> clientEmpDropDown(@Param("id") int id);

	@Query(nativeQuery = true, value = "SELECT master_emp_details.emp_id AS empId, master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName, master_emp_details.employee_id AS lancesoftId, designations.desg_names AS desgName FROM(master_emp_details LEFT JOIN designations ON master_emp_details.desg_fk=designations.desg_id) WHERE designations.desg_names='HR' OR master_emp_details.desg_fk= ?1 ")
	public List<SupervisorDropDown> supDropDown(int id);

	@Query(nativeQuery = true, value = "SELECT master_emp_details.employee_id As lancesoftId,master_emp_details.emp_id As empId, master_emp_details.first_name AS firstName, master_emp_details.last_name AS lastName FROM master_emp_details where master_emp_details.supervisor_fk= ?1 ")
	public List<ClientEmpDropDownResponse> clientEmpDropDowns(@Param("id") int id);

	@Query(nativeQuery = true, value = "SELECT master_emp_details.emp_id AS empId, master_emp_details.employee_id AS lancesoftId, master_emp_details.first_name AS firstName,master_emp_details.last_name AS lastName, total_emp_expenses.start_date AS startDate, total_emp_expenses.end_date AS endDate, total_emp_expenses.total AS empTotal, total_emp_expenses.total_emp_exp_id AS totalEmpExpId FROM(total_emp_expenses LEFT JOIN master_emp_details ON total_emp_expenses.emp_id_fk=master_emp_details.emp_id) WHERE master_emp_details.supervisor_fk= ?1 OR master_emp_details.emp_id= ?1 ")
	public List<InitialExpenseDetailResponse> getCardDetailOfExpenses(@Param("loggedInId") int loggedInId);

	@Query(nativeQuery = true, 
			value = "WITH RECURSIVE emp_ETC as (Select emp_id, first_name,last_name, supervisor_fk, desg_fk,emp_photo_id from master_emp_details "
					+ "UNION Select m.emp_id, m.first_name, m.last_name, m.supervisor_fk, m.desg_fk,m.emp_photo_id "
					+ "FROM emp_ETC e "
					+ "LEFT JOIN master_emp_details m "
					+ "ON e.emp_id=m.supervisor_fk "
					+ "LEFT JOIN employee_photo p "
					+ "ON e.emp_photo_id =p.doc_id "
					+ "LEFT JOIN designations d "
					+ "ON e.desg_fk=d.desg_id) "
					+ "SELECT e2.emp_id AS empId, m2.employee_id AS lancesoftId, CONCAT(e2.first_name,' ',e2.last_name) AS employeeName, "
					+ "CONCAT(m2.first_name,' ',m2.last_name) As managerName, d2.desg_names AS designation, p2.profile_pic AS Photo "
					+ "FROM emp_ETC e2 "
					+ "LEFT JOIN  master_emp_details m2 "
					+ "ON m2.emp_id=e2.supervisor_fk "
					+ "LEFT JOIN employee_photo p2 "
					+ "ON p2.doc_id=e2.emp_photo_id "
					+ "LEFT JOIN  designations d2 "
					+ "ON d2.desg_id=e2.desg_fk ORDER BY ?#{#pageRequest}",
			countQuery = "SELECT count(*) FROM master_emp_details ")
	public Page<AllEmpCardDetails> getAlEmpCardDetails(PageRequest pageRequest);
	
	
	@Query(nativeQuery = true, 
			value = "WITH RECURSIVE emp_ETC as (Select emp_id, first_name,last_name, supervisor_fk, desg_fk,emp_photo_id from master_emp_details "
					+ "UNION Select m.emp_id, m.first_name, m.last_name, m.supervisor_fk, m.desg_fk, m.emp_photo_id "
					+ "FROM emp_ETC e "
					+ "LEFT JOIN master_emp_details m "
					+ "ON e.emp_id=m.supervisor_fk "
					+ "LEFT JOIN employee_photo p "
					+ "ON e.emp_photo_id =p.doc_id "
					+ "LEFT JOIN designations d "
					+ "ON e.desg_fk=d.desg_id) "
					+ "SELECT e2.emp_id AS empId, m2.employee_id AS lancesoftId, CONCAT(e2.first_name,' ',e2.last_name) AS employeeName, "
					+ "CONCAT(m2.first_name,' ',m2.last_name) As managerName, d2.desg_names AS designation, p2.profile_pic AS Photo "
					+ "FROM emp_ETC e2 "
					+ "LEFT JOIN  master_emp_details m2 "
					+ "ON m2.emp_id=e2.supervisor_fk "
					+ "LEFT JOIN employee_photo p2 "
					+ "ON p2.doc_id=e2.emp_photo_id "
					+ "LEFT JOIN  designations d2 "
					+ "ON d2.desg_id=e2.desg_fk WHERE desg_id= ?1 ORDER BY ?#{#pageRequest} ",	
			countQuery = "SELECT count(*) FROM master_emp_details ")
	public Page<AllEmpCardDetails> getSortedEmpCardDetailsByDesg(@Param("desgId")Integer desgId,PageRequest pageRequest);
	
	@Query(nativeQuery = true,
			value="WITH RECURSIVE emp_ETC as ("
					+ "SELECT emp_id, first_name,last_name, supervisor_fk, desg_fk, employee_id, emp_photo_id, status "
					+ "FROM master_emp_details "
					+ "UNION "
					+ "SELECT m.emp_id, m.first_name, m.last_name, m.supervisor_fk, m.desg_fk,m.employee_id, m.emp_photo_id, m.status "
					+ "FROM emp_ETC e "
					+ "LEFT JOIN master_emp_details m "
					+ "ON e.emp_id=m.supervisor_fk "
					+ "LEFT JOIN employee_photo p "
					+ "ON e.emp_photo_id =p.doc_id "
					+ "LEFT JOIN master_emp_details m4 "
					+ "ON e.employee_id=m4.employee_id "
					+ "LEFT JOIN designations d "
					+ "ON e.desg_fk=d.desg_id) "
					+ "SELECT e2.emp_id AS empId, e2.employee_id AS lancesoftId, e2.status AS status, CONCAT(e2.first_name,' ',e2.last_name) AS employeeName, "
					+ "CONCAT(m2.first_name,' ',m2.last_name) As managerName, d2.desg_names AS designation, p2.profile_pic AS Photo "
					+ "FROM emp_ETC e2  "
					+ "LEFT JOIN  master_emp_details m2 "
					+ "ON m2.emp_id=e2.supervisor_fk "
					+ "LEFT JOIN  designations d2 "
					+ "ON d2.desg_id=e2.desg_fk "
					+ "LEFT JOIN employee_photo p2 "
					+ "ON p2.doc_id=e2.emp_photo_id "
					+ "LEFT JOIN master_emp_details m5 "
					+ "ON m5.employee_id=e2.employee_id "
					+ "WHERE e2.last_name LIKE %?1% OR "
					+ "e2.first_name LIKE %?1% OR "
					+ "e2.status LIKE %?1% OR "
					+ "m5.employee_id LIKE %?1% OR "
					+ "d2.desg_names LIKE %?1% "
					+ "ORDER BY m5.employee_id DESC")
	public  List<AllEmpCardDetails> searchByIDNameDesg(@Param("keyword")String keyword);


    @Query("FROM MasterEmployeeDetails g where g.designations.id = :designations")
	List<MasterEmployeeDetails>  findBydesignations_Id(@Param("designations")Integer  id);
    
    
    

    @Query("FROM MasterEmployeeDetails g where g.designations.id = :designations")
	MasterEmployeeDetails  findBydesignations_Id1(@Param("designations")Integer  id);
    
    
    public List<MasterEmployeeDetails>  findByDesignations(Designations designations);

	public List<MasterEmployeeDetails> findByDesignationsAndSupervisor(Designations desg,MasterEmployeeDetails supervisor);

	public List<MasterEmployeeDetails> findBySupervisor(MasterEmployeeDetails supEmp);
	
	public Page<MasterEmployeeDetails> findBySupervisor(MasterEmployeeDetails supEmp,PageRequest pageRequest);

	public List<MasterEmployeeDetails> findBySupervisorAndDesignations(MasterEmployeeDetails supEmp,Designations desg);
	 @Query ("SELECT COUNT(ra) FROM MasterEmployeeDetails ra WHERE ra.status ='ACTIVE' or ra.status='BENCH' or ra.status='MANAGMENT'")
	public Long countNoOfRecord();

	public Page<MasterEmployeeDetails> findAllByStatusIn(PageRequest of, List<EmployeeStatus> statuses);

//	public Page<MasterEmployeeDetails> findBySupervisorAndStatusIn(MasterEmployeeDetails loggedInEmployee,
//			PageRequest of ,List<EmployeeStatus> status);

//	public Page<MasterEmployeeDetails> findBySupervisor(MasterEmployeeDetails loggedInEmployee,
//			PageRequest of, List<EmployeeStatus> status);

	public List<MasterEmployeeDetails> findBySubDepartmentsAndDesignationsAndLancesoftContainingOrFirstNameContainingOrLastNameContaining(SubDepartments SubDepartmentNames,
			Designations DesgNames,String keyword1,String keyword2,String keyword3);

	public List<MasterEmployeeDetails> findByDesignationsAndFirstNameContainingOrLastNameContainingOrLancesoftContaining(Designations designations,
			String keyword, String keyword2,String keyword3);

	public List<MasterEmployeeDetails> findByDesignationsAndStatusIn(Designations designations, List<EmployeeStatus> status);

	public List<MasterEmployeeDetails> findAllByDesignationsAndStatusIn(Designations designations,
			ArrayList<EmployeeStatus> statusList);
    

    @Query("FROM MasterEmployeeDetails g where g.designations.id = :designations")
  List<MasterEmployeeDetails> findByAlldesgIdWithNames(@Param("designations")Integer id);
    

	
//    @Modifying
//    @Query(value = " DELETE FROM address WHERE emp_id_fk = :empId ; DELETE FROM employee_photo WHERE emp_id_fk = :empId ; DELETE FROM employees_at_clients_details WHERE emp_id_fk = :empId ; DELETE FROM master_emp_details WHERE emp_id = :empId ; DELETE FROM attachment WHERE emp_fk = :empId ; DELETE FROM internal_expenses WHERE emp_id_fk = :empId ; DELETE FROM release_emp_details WHERE master_employee_details_id_fk = :empId ; DELETE FROM salary WHERE emp_id_fk = :empId ; DELETE FROM secondary_manager WHERE employee_emp_id = :empId ; DELETE FROM sub_profit WHERE emp_id_fk = :empId ;", nativeQuery = true)
//    public void deleteFromMultipleTables(@Param("empId") Long empId);
//        // method body is empty
//   
    
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Address a WHERE a.masterEmployeeDetails = :empId "+
//   " DELETE FROM EmployeePhoto ep WHERE ep.masterEmployeeDetails = :empId "+
//   " DELETE FROM EmployeesAtClientsDetails ecd WHERE ecd.masterEmployeeDetails = :empId "+
//   " DELETE FROM MasterEmpDetails med WHERE med.empId = :empId "+
//   " DELETE FROM Attachment at WHERE at.masterEmployeeDetails = :empId"+
//   " DELETE FROM InternalExpenses ie WHERE ie.masterEmployeeDetails = :empId "+
//   " DELETE FROM ReleaseEmpDetails red WHERE red.masterEmployeeDetailsId = :empId "+
//   " DELETE FROM Salary s WHERE s.masterEmployeeDetails = :empId "+
//   " DELETE FROM SecondaryManager sm WHERE sm.employee = :empId "+
//   " DELETE FROM SubProfit sp WHERE sp.masterEmployeeDetails = :empId ")
//    public void deleteFromMultipleTables(@Param("empId") Integer empId);

	
	


}
