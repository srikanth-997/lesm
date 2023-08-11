package ls.lesm.repository.recruiter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.recruiter.CandidateProfiles;
import ls.lesm.model.recruiter.CandidateStatus;

public interface CandidateStatusRepo extends JpaRepository<CandidateStatus, Integer> {

	Optional<CandidateStatus> findByCandidateProfiles(CandidateProfiles profile);

}
