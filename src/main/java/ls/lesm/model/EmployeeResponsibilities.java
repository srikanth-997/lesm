package ls.lesm.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import ls.lesm.model.enums.ResponsibilitiesTypes;

@Setter
@Getter
@Entity
public class EmployeeResponsibilities {
	

	@Id
	@GeneratedValue(generator = "resp_id_gen",strategy = GenerationType.AUTO)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private ResponsibilitiesTypes responsibilitiesTypes;
	
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

}
