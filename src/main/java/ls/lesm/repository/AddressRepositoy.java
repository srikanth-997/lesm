package ls.lesm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.Address;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.payload.response.EmployeeDetailsResponse;
import ls.lesm.payload.response.ForeignkeyResponse;

public interface AddressRepositoy extends JpaRepository<Address, Integer> {

	
   
    @Query("FROM Address a where a.masterEmployeeDetails.id= :masterEmployeeDetailsId")
   public List<Address> findByEmpIdFk(@Param("masterEmployeeDetailsId")int id);
    
    @Query("FROM Address a where a.masterEmployeeDetails.id= :masterEmployeeDetailsId")
    public Address findByEmpId(@Param("masterEmployeeDetailsId")int id);
   
  @Query(nativeQuery = true, value="SELECT address.emp_id_fk AS fKey FROM address WHERE address.emp_id_fk= ?1 ")
   public List<ForeignkeyResponse> getKey(int id);

public List<Address> findByMasterEmployeeDetails(MasterEmployeeDetails emp);

//public Address findByMasterEmployeeDetails(MasterEmployeeDetails empp);


	

}
