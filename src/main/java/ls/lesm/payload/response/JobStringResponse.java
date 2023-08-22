package ls.lesm.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class JobStringResponse {

	private Integer jobStringId;

	private String JD;

	private Integer totalPosition;

	private String stringCreatedBy;

	private String budget;

	private String openDate;

	private String createdAt;

	private String closeDate;

	private String clientName;

	private String sampleResume;

	private String jobStringTicket;

	private boolean ticketStatus;
	
	private String hiringType;

	private List<TaggedJobResponse> taggedEmployees;
}
