package ls.lesm.model.timesheet;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.recruiter.Status;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Approval implements Serializable {
	
	@Id
	@GeneratedValue(generator = "int_tagden", strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;
	@Column(name = "EditedAt")
	private LocalDateTime editedAt;
	@Column(name = "CreatedLoginId")
	private Integer createdLoginId;
	@Column(name = "EditedLoginId")
	private Integer editedLoginId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TimeSheetEntry")
	private TimeSheetEntry timeSheetEntry;

	@Column(name = "ApprovalStatus")
	private Status approvalStatus;

	private LocalDateTime approvedAt;

	@Column(name = "Comment")
	private String comment;

	@ManyToOne()
	@JoinColumn(name = "ManagerId")
	private MasterEmployeeDetails managerId;
}
