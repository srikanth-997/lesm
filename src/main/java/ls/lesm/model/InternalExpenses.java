package  ls.lesm.model;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "emp_id_fk"}) })
public class InternalExpenses {
	
	public InternalExpenses(MasterEmployeeDetails masterEmployeeDetails) {
		super();
		this.masterEmployeeDetails = masterEmployeeDetails;
	}
	@Id
	@GeneratedValue(generator = "int_exp_gen",strategy = GenerationType.AUTO)
	private Integer internalExpId;
	private long benchTenure=0l;// PO end date to sysdate or po start date
	private Double totalSalPaidTillNow;// salary*bechTenure
	private Double totalExpenses;// (salary+foodCost+cubicleCost+transportationCost)*benchTenure=totalExpenses
	private Double profitOrLoss;// totalExpenses-totalEarningAtClients from (EmployeesAtClientsDetails table) if that expense from is internal will add that
	
	//@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate createdAt;//timpStamp
	
	
	@Column(length=30)
	private String createdBy;//principal
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_id_fk",unique = true)
	private MasterEmployeeDetails masterEmployeeDetails;
	
	private Double BR_INR;
	
	
	private Double BR_USD;
	
	private Double PR_INR;
	
	private Double PR_USD;
	
	private Double GPM_INR;
	
	private Double GPM_USD;
	
	private Double GM;
	
	private Double benchPay;
	
	private long daysOnBench;
	
	
	
}
