package ls.lesm.model.timesheet;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class HolidayCalender {
	
	@Id
	@GeneratedValue(generator = "int_holiden", strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;
	@Column(name = "EditedAt")
	private LocalDateTime editedAt;
	@Column(name = "CreatedLoginId")
	private Integer createdLoginId;
	@Column(name = "EditedLoginId")
	private Integer editedLoginId;
	
	@Column(nullable = false, unique = true)
	private LocalDate holidayDate;
	
	private String holidayName;
	
	@Column(name="Description")
	private String desc;
	
	private Integer year;
	
}
