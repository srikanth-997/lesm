package ls.lesm.repository.recruiter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.recruiter.CandidateProfiles;
import ls.lesm.model.recruiter.JobString;

public interface CandidateProfilesRepo extends JpaRepository<CandidateProfiles, String> {

	List<CandidateProfiles> findByMasterEmployeeDetailsAndJobString(MasterEmployeeDetails employee,
			JobString jobString);

	List<CandidateProfiles> findByJobString(JobString jobString);
	

}
