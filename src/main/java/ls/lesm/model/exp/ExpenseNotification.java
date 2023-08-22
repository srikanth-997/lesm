package ls.lesm.model.exp;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExpenseNotification {
	
	@Id
	@GeneratedValue(generator = "noti_id_gen",strategy = GenerationType.AUTO)
	private Integer notificationId;
	
	private boolean flag;
	
	private String message;
	
	private LocalDateTime createdAt;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

}
