package ls.lesm.payload.response;

import lombok.Data;
import ls.lesm.model.enums.EmployeeStatus;

@Data
public class PageResponse {
	
	private Integer empId;
	private String designation;
	private String managerName;
	private String subordinateManagerName;
	private String photo;
	private String employeeName;
	private String lancesoftId;
	private EmployeeStatus status;

}
