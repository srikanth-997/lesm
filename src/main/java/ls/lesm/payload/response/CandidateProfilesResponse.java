package ls.lesm.payload.response;

import lombok.Data;
import ls.lesm.model.recruiter.Status;

@Data
public class CandidateProfilesResponse {
	
	private String candidateId;
	
	private String candidateName;
	
	private String emailId;
	
	private Long mobileNo;
	
	private Integer currentCTC;
	
	private Integer expectedCTC;
	
	
	private String relevantExp;
	
    private String sentBy;
	
	private String totalExp;
	

	private String currentOrg;
	
	private String candiResume;
	

	private String sentAt;
	
	
	private Status managerApproval;
	
	private String approveAt;
	
	
	private String ticketName;

}
