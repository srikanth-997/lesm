package ls.lesm.model.timesheet;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeavePolicy {
	
	@Id//not auto inc
	private Short id;
	
	private LocalDateTime updateAt;
	
	@OneToOne
	@JoinColumn(name="LeaveType")
	private EntryType leaveType;
	
	@Column(name="MonthlyIncreas",scale = 2,precision = 2)
	private Float monthlyIncreas;
	
	@Column(name="IsCarryForward")
	private boolean isCarryForward;
	

}
