package ls.lesm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.recruiter.Status;

@Setter 
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequest {
	
	private Integer id;
	
	private String comment;
	
	private Status status;

}
