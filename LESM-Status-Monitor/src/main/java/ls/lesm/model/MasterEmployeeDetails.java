package  ls.lesm.model;


import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.recruiter.JobString;

@Entity
@Table(name="Master_EMP_Details")
@Setter
@Getter	
@AllArgsConstructor
@NoArgsConstructor
public class MasterEmployeeDetails{

	@Id
	@GeneratedValue(generator = "emp_id_gen",strategy = GenerationType.AUTO)
	private Integer empId;// AutoInc Pk
	
	@Column(length=30,name="employee_id")
//	@NotNull(message="lancesoft id is not null")
//	@NotBlank(message="lancesoft id is not be blank")
	private String lancesoft;// lancesoft Id	
	
	@Column(length=30)
//	@NotNull(message="firstName  is not null")
//	@NotBlank(message="firstName  is not be blank")
	private String firstName;
	
	@Column(length=30)
	private String lastName;
	
	private LocalDate joiningDate;
	
	private LocalDate DOB;
	
	
	private String location;
	
	
	private String gender;
	
	
	private String email;

	private LocalDate createdAt;
	
	private String vertical;
	
	@Column(nullable = true)
	private String PANNumber;
	
	@Column(nullable = true)
	private Integer aadharNumber;
	
	@Enumerated(EnumType.STRING)
	private EmployeeStatus status;// active/bench/releas
	
	private Integer Age;// dob+sysDate
	private Boolean isInternal;// employee is internal or external
	
	private Long phoneNo;
	
	private String createdBy;
	
	private String technology2;
	
	//private LocalDate exitAt;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sub_depart_fk")
	private SubDepartments subDepartments;
	
	
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="department_fk")
	private Departments departments;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="Desg_fk")
	private Designations designations;
	
	/*@OneToOne(cascade=CascadeType.ALL)
	@JoinTable(name="employee_relation",
	joinColumns=@JoinColumn (name="emp_id"),
	inverseJoinColumns =@JoinColumn(name="supervisor_id"))
	private MasterEmployeeDetails masterEmployeeDetails;*/
	
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="supervisor_fk")
	private MasterEmployeeDetails supervisor;
	
/*	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
	//@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="verticle_fk")
	private MasterEmployeeDetails verticle;*/
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_type_fk")
	private EmployeeType employeeType;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="technologyId")
	private Technology technology1;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name="emp_photo_id")
	private EmployeePhoto employeePhoto;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
	@ManyToMany(fetch = FetchType.LAZY,

			mappedBy = "masterEmployeeDetails")
	@JsonIgnore
	private List<JobString> jobString;
	

}
