package ls.lesm.model.timesheet;

import java.io.Serializable;
import java.time.LocalDate;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalProject;
import ls.lesm.model.MasterEmployeeDetails;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "int_tgen", strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;
	@Column(name = "EditedAt")
	private LocalDateTime editedAt;
	@Column(name = "CreatedLoginId")
	private Integer createdLoginId;
	@Column(name = "EditedLoginId")
	private Integer editedLoginId;

	@Column(name = "ODDate")
	private LocalDate odDate;

	@Column(name = "IsInternal")
	private boolean isInternal;

	@Column(name = "Reason")
	private String reason;
	
	@Column(name = "Device", length = 15)
	private String device;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ModeId")
	private Mode modeId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EntryTypeId")
	private EntryType entryTypeId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ShiftTypeId")
	private ShiftType shiftTypeId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ProjectId")
	private InternalProject projectId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EmployeeId")
	private MasterEmployeeDetails employeeId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EmployeesAtClientsId")
	private EmployeesAtClientsDetails employeesAtClientsId;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER,mappedBy = "timeSheetEntry")
	@JoinColumn(name="ApprovalId")
	private Approval approvalId;
}
