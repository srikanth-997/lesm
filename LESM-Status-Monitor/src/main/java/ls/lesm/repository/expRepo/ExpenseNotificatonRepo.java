package ls.lesm.repository.expRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ls.lesm.model.Address;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.exp.ExpenseNotification;

@Repository
public interface ExpenseNotificatonRepo extends JpaRepository<ExpenseNotification, Integer> {

	 
    @Query("FROM ExpenseNotification a where a.masterEmployeeDetails.id= :masterEmployeeDetailsId")
   public List<ExpenseNotification> findByEmpIdFk(@Param("masterEmployeeDetailsId")int id);

	public List<ExpenseNotification> findByMasterEmployeeDetailsAndFlag(MasterEmployeeDetails employee, boolean b);
}
