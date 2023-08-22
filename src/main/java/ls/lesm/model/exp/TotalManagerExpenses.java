package ls.lesm.model.exp;

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
import ls.lesm.model.MasterEmployeeDetails;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TotalManagerExpenses {
	
	@Id
	@GeneratedValue(generator = "expmgr_id_gen",strategy = GenerationType.AUTO)
	private Integer totalManagerExpId;
	
	private Double travel;
	
	private Double accomodation;
	
	private Double food;
	
	private Double others;
	
	private Double allowance;
	
	private Double total;
	
	private boolean status;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_total_id")
	private TotalEmpExpenses totalEmpExpenses;
	
	

}
