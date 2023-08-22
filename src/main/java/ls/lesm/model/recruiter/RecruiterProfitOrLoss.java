package ls.lesm.model.recruiter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;

@Setter
@Getter
@Entity
public class RecruiterProfitOrLoss {
	@Id
	@GeneratedValue(generator = "prftlss_id_gen",strategy = GenerationType.AUTO)
	private Integer profitOrLossId;
	private Double profitOrLoss;
	

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_rec_id")
	private MasterEmployeeDetails masterEmployeeDetails;

}
