package  ls.lesm.model;
import java.time.LocalDate;
import java.util.Date;

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
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ls.lesm.model.enums.PaidStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OnsiteBillExpenses {
	
	@Id
	@GeneratedValue(generator = "site_bexp_gen",strategy = GenerationType.AUTO)
	private Integer billExpId;
	private Double cab;
	private Double travel;
	private Double accommodation;
	private Double food;
	
	
	private LocalDate travelSDate;

	private LocalDate travelEDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private PaidStatus paidStatus;
	
	private String totalTravelPeriod;
	
	@JsonIgnore
	private Date createdAt;//timpStamp
	
	@JsonIgnore
	@Column(length=30)
	private String createdBy;//principal
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exp_type_fk")
	private OnsiteExpensesType onsiteExpensesType;
	
	@JsonIgnore

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id_fk")
	private  MasterEmployeeDetails masterEmployeeDetails;
	

}
