package ls.lesm.payload.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.InternalProject;
import ls.lesm.model.recruiter.Status;
import ls.lesm.model.timesheet.EntryType;
import ls.lesm.model.timesheet.Mode;
import ls.lesm.model.timesheet.ShiftType;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetHistoryResponse {

	private Integer id;

	private LocalDateTime createdAt;

	private Integer createdLoginId;

	private String odDate;

	private boolean isInternal;

	private String reason;

	private Mode modeId;
	private String device;
	private EntryType entryTypeId;

	private ShiftType shiftTypeId;

	private InternalProject projectId;

	private Integer employeeId;

	private String employeeName;

	private Integer employeeAtClientDetailId;
	private String clientName;
	private Status ApproveStatus;
	private String approveAt;
	private String comment;
	private Integer managerId;
	private String managerName;

}
