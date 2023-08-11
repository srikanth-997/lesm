package ls.lesm.model.timesheet;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeaveBalance {
	
	@Id
	@GeneratedValue(generator = "int_lbden", strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="CreatedAt")
	private LocalDateTime createdAt;
	
	@Column(name="UpdatedAt")
	private LocalDateTime updatedAt;
	
	private Integer year;
	
	
	@Column(name="Balance",scale = 4,precision = 2)
	private Float balance;
	
	@Column(name="Laps",scale = 4,precision = 2)
	private Float laps;
	
	@Column(name="Used")
	private Integer used;
	
	@ManyToOne()
	@JoinColumn(name = "EntyTypeId")
	private EntryType leaveType;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "EmployeeId")
	private MasterEmployeeDetails employee;
}
