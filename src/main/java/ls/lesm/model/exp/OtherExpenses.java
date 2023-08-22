package ls.lesm.model.exp;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class OtherExpenses {
	
	@Id
	@GeneratedValue(generator = "othExp_id_gen",strategy = GenerationType.AUTO)
	private Integer otherExpId;
	
	private String expenseName;
	
	private Double amount;
	
	private String billPath;
	
	private String reason;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String location;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="manager_status_id")
	private ManagerApprovalStatus managerApprovalStatus;
	

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="finance_status_id")
	private FinanceApprovalStatus financeApprovalStatus;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="total_id")
	private TotalEmpExpenses totalEmpExpenses;

}
