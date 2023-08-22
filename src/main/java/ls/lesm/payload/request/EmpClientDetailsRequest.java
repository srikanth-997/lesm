package ls.lesm.payload.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data 
@AllArgsConstructor
@NoArgsConstructor
//UMER
public class EmpClientDetailsRequest {
	

	private Double clientSalary;
	private LocalDate POSdate;// purchase order start date
	private LocalDate POEdate;// purchase order end dat
	private String desgAtClient;
	
	
	private LocalDate createdAt;//timpStamp
	


	private String createdBy;//principal
	
	private Integer tenure;
	private Double totalEarningAtClient;

}
