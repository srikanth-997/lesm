package ls.lesm.model.exp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@RequiredArgsConstructor
public class ManagerApprovalStatus {

	@Id
	@GeneratedValue(generator = "mapprove_id_gen",strategy = GenerationType.AUTO)
	private Integer mApproveId;
	
	@NonNull
	@Enumerated(EnumType.STRING)
	private Status status;
	
	private Double approvedAmount;
	
	private String remark;
	
	private String approvedBy;
	

	
	@NonNull
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	
}
