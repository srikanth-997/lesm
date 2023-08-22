package ls.lesm.model.exp;

import java.time.LocalDate;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TotalEmpExpenses {
	
	@Id
	@GeneratedValue(generator = "expemp_id_gen",strategy = GenerationType.AUTO)
	private Integer totalEmpExpId;
	
	private Double travel;
	
	private Double accomodation;
	
	private Double food;
	
	private Double others;
	
	private Double allowance;
	
	private Double total;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	@Enumerated(EnumType.STRING)
	private ExpenseType expenseType;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	

}
