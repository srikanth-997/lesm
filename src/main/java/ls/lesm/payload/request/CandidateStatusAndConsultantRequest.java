package ls.lesm.payload.request;

import lombok.Data;
import ls.lesm.model.recruiter.CandidateStatus;
import ls.lesm.model.recruiter.Consultant;
@Data
public class CandidateStatusAndConsultantRequest {
	
   private CandidateStatus candidateStatus;
   private Consultant consultans;

}
