package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Salary {
	
	@Id
	@GeneratedValue(generator = "sal_gen",strategy = GenerationType.AUTO)
	private Integer salId;
	//private Boolean isLatest;// 0 old salary; 1 new salary
	private Double salary;
	
	@JsonIgnore
	private LocalDate createdAt;
	
	
	private LocalDate updatedAt;
	
	@JsonIgnore
	@Column(length=30)
	private String createdBy;//principal
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
    private MasterEmployeeDetails masterEmployeeDetails;

//	public Salary(Double salary, LocalDate updatedAt, MasterEmployeeDetails masterEmployeeDetails) {
//		super();
//		this.salary = salary;
//		this.updatedAt = updatedAt;
//		this.masterEmployeeDetails = masterEmployeeDetails;
//	}

	public Salary(Double salary, LocalDate createdAt, LocalDate updatedAt, String createdBy,
			MasterEmployeeDetails masterEmployeeDetails) {
		super();
		this.salary = salary;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.masterEmployeeDetails = masterEmployeeDetails;
	}
	
	

//	public Salary(Double salary, LocalDate updatedAt, MasterEmployeeDetails masterEmployeeDetails) {
//		super();
//		this.salary = salary;
//		UpdatedAt = updatedAt;
//		this.masterEmployeeDetails = masterEmployeeDetails;
//	}
	

}
