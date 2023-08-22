package ls.lesm.payload.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.timesheet.TimeSheetEntry;

@Setter 
@Getter 
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLinesResponse {
	
	private Integer id;
	
	private String employeeName;
	
	private String employeePhoto;
	
	private String lancesoftId;
	
	private Integer allRequest;
	
	private Integer odRequest;
	
	private Integer lopRequest;
	
	private Integer extraWorkRequst;
	
	private Integer slRequest;
	private Integer elRequest;
	private Integer clRequest;
	private Integer hodRequest;
	private Integer phRequest;
	
	private List<TimeSheetHistoryResponse> ods;
	private Map<String ,List<TimeSheetHistoryResponse>> leaves;

	private List<TimeSheetHistoryResponse> extraWork;
	private List<TimeSheetHistoryResponse> hod;
	private List<TimeSheetHistoryResponse> ph;
	
	
	
	
	

}
