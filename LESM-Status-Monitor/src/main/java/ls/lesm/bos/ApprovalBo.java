package ls.lesm.bos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.recruiter.Status;
import ls.lesm.model.timesheet.TimeSheetEntry;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalBo {

	private Integer id;

	private LocalDateTime createdAt;

	private LocalDateTime editedAt;

	private Integer createdLoginId;

	private Integer editedLoginId;

	private TimeSheetEntry timeSheetEntry;

	private Status approvalStatus;

	private LocalDateTime approvedAt;

	private String comment;

	private MasterEmployeeDetails managerId;

}
