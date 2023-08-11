package ls.lesm.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class AtClientAllowances {

	@Id
	@GeneratedValue(generator = "client_alwn_id_gen", strategy = GenerationType.AUTO)
	private Integer atClientAllowancesId;

	private Double shiftAllowance;

	private Double specialAllowance;

	private Double joingBonus;

	private Integer joiningBonusTenure;
	
	
	private Double extraAllowance;
	
	private Double DeputationAllowances;

	@JsonIgnore
	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

	public AtClientAllowances() {
		super();
	}

	public AtClientAllowances( Double shiftAllowance, Double specialAllowance,
			Double joingBonus, Integer joiningBonusTenure, Double extraAllowance, Double deputationAllowances,
			MasterEmployeeDetails masterEmployeeDetails) {
		super();
		
		this.shiftAllowance = shiftAllowance;
		this.specialAllowance = specialAllowance;
		this.joingBonus = joingBonus;
		this.joiningBonusTenure = joiningBonusTenure;
		this.extraAllowance = extraAllowance;
		DeputationAllowances = deputationAllowances;
		this.masterEmployeeDetails = masterEmployeeDetails;
	}

	
	

}
