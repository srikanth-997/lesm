package ls.lesm.model.timesheet;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftType {

	@Id
	@GeneratedValue(generator = "int_tgssen", strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;
	@Column(name = "EditedAt")
	private LocalDateTime editedAt;
	@Column(name = "CreatedLoginId")
	private Integer createdLoginId;
	@Column(name = "EditedLoginId")
	private Integer editedLoginId;

	@Column(length = 8, name = "ShiftCode")
	@NotBlank(message = "Maximum 8 Char only")
	private String shiftCode;

	@Column(name = "ShiftDesc")
	private String shftDesc;

	@Column(name = "StartTime")
	private LocalTime startTime;

	@Column(name = "EndTime")
	private LocalTime endTime;

}
