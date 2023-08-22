package ls.lesm.payload.request;

import lombok.Data;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;

@Data
public class TransferRequest {
	public MasterEmployeeDetails masterEmployeeDetails;
	public Salary salary;
	
}