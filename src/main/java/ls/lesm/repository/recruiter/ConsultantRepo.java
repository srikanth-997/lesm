package ls.lesm.repository.recruiter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.recruiter.Consultant;

public interface ConsultantRepo extends JpaRepository<Consultant, String> {

	List<Consultant> findByMasterEmployeeDetailsIn(List<MasterEmployeeDetails> allRec);

}
