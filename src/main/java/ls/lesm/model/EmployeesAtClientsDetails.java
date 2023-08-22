package ls.lesm.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ls.lesm.model.enums.WorkMode;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EmployeesAtClientsDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "emp_at_cdetails_gen", strategy = GenerationType.AUTO)
	private Integer EmpAtClientId;
	private Double clientSalary;
	private LocalDate POSdate;// purchase order start date

	@Column(nullable = true)
	private LocalDate POEdate;// purchase order end date

	@Column(length = 30)
	private String desgAtClient;

	// private Long clientTenure;// toatl months at client(posdate to poedate)
	// private Double totalEarningAtClients;// clientTenure*cliendt salary

	// new fields added on 25-01-2023
	private Long POValue;
	//@Column(unique = true)- removed (if 5 employee placed for one po number in that case unique will not work)
	private String PONumber;
	private Double IGST;
	private Double CGST;
	private Double SGST;
	private Double totalTax;
	private LocalDate PODate;

	private String skillSet;
	private LocalDate offerReleaseDate;
	private LocalDate clientLastWorkingDate;
	private LocalDate lancesoftLastWorkingDate;
	private LocalDate clientJoiningDate;
	private String clientLocation;

	@Enumerated(EnumType.STRING)
	private WorkMode workMode;

	@JsonIgnore
	private LocalDate createdAt;// timpStamp

	@JsonIgnore
	@Column(length = 30)
	private String createdBy;// principal

	private String clientEmail;

	private String clientManagerName;


	private Long clientTenure;

	private Double totalEarningAtclient;

	@JsonIgnore
	// @Fetch(FetchMode.JOIN)
	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	private MasterEmployeeDetails towerHead;
	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	private MasterEmployeeDetails towerLead;
	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@ManyToOne(fetch = FetchType.LAZY)
	private MasterEmployeeDetails recruiter;

	// @JsonIgnore
	// @Fetch(FetchMode.JOIN)
	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "clients_fk")
	private Clients clients;
	
	@JsonIgnoreProperties({ "hibernateLazyInitializer" })
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subContractor")
	private Clients subContractor;
	
}
