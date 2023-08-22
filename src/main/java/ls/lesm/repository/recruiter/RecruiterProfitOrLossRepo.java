package ls.lesm.repository.recruiter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.recruiter.RecruiterProfitOrLoss;

public interface RecruiterProfitOrLossRepo extends JpaRepository<RecruiterProfitOrLoss, Integer> {

	//List<RecruiterProfitOrLoss> findByMasterEmployeeDetails(List<MasterEmployeeDetails> allRec);

	//List<RecruiterProfitOrLoss> findByMasterEmployeeDetailsIn(List<MasterEmployeeDetails> allRec);

	List<RecruiterProfitOrLoss> findByMasterEmployeeDetailsIn(List<MasterEmployeeDetails> allRec);

}
