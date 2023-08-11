package ls.lesm.bos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalProject;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.timesheet.Approval;
import ls.lesm.model.timesheet.EntryType;
import ls.lesm.model.timesheet.Mode;
import ls.lesm.model.timesheet.ShiftType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetEntryBo {

	private Integer id;
	private LocalDateTime createdAt;
	private LocalDateTime editedAt;
	private Integer createdLoginId;
	private Integer editedLoginId;

	private LocalDate odDate;

	private boolean isInternal;

	private String reason;

	private Mode modeId;

	private String device;
	private EntryType entryTypeId;

	private ShiftType shiftTypeId;

	private InternalProject projectId;

	private MasterEmployeeDetails employeeId;

	private EmployeesAtClientsDetails employeesAtClientsId;
	private Approval approvalId;
}
