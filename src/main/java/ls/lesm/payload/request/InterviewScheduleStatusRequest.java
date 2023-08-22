package ls.lesm.payload.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ls.lesm.model.recruiter.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScheduleStatusRequest {
	
    private Integer statusId;
	
	private String L1ScheduleAt;
	
	private String L1PostponedAt;
	
	private Status L1Status;
	
	private String L2ScheduleAt;
	
	private String L2PostponedAt;
	
	private Status L2Status;
	
	private boolean releasedOffer;//yes/no
	
	private Status profileApproval;
	
	private LocalDate releasedOfferAt;
	
	private boolean joined;//yes/no
	
	
	private String RecId;
	
	private String recruiterName;
	
	private Integer jobStringId;
	
	private String condiProfileId;
	
	private String candiName;

}
