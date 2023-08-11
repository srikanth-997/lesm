package ls.lesm.payload.request;

import lombok.Data;
import ls.lesm.model.recruiter.Status;
@Data
public class ApproveProfilesRequest {
	
	
	private String id;
	private Status status;

}
