package ls.lesm.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class SecondaryManager {
	@Id
	@GeneratedValue(generator = "sec_sup_gen",strategy = GenerationType.AUTO)
	private Integer id;
	

	@OneToOne
	private MasterEmployeeDetails employee;
	@OneToOne
	private MasterEmployeeDetails  secondaryManager;
	

}
