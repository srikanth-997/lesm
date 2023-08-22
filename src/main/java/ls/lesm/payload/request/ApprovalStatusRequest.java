package ls.lesm.payload.request;

import lombok.Getter;
import lombok.Setter;
import ls.lesm.model.exp.Status;
import ls.lesm.model.exp.TotalManagerExpenses;
@Setter
@Getter
//UMER
public class ApprovalStatusRequest {
	

    private	Integer expenseId;

	private Status status;
	
	private Double approvedAmount;
	
	private String remark;
	
	private String approvedBy;
	
	
	

}
