package ls.lesm.payload.response;

import java.time.LocalDate;

import lombok.Data;
import ls.lesm.model.Clients;

@Data
public class EmployeesAtClientsDetailsResponse {
	
	
	private Integer EmpAtClientId;
	private String clientSalary;
	private String POSdate;// purchase order start date
	
	private String POEdate;// purchase order end date
	
	private String desgAtClient;

	private String clientEmail;
	
	private String clientManagerName;
	
	private Clients clients;
	
	private String tenure;
	
	private String totalEarningAtclient;
	
	private String POValue;
	private String PONumber;
	private String IGST;
	private String CGST;
	private String SGST;
	private String totalTax;
	private String handledBy;
	private String towerHead;
	private String PODate;

}
