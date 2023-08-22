package ls.lesm.payload.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import ls.lesm.model.enums.WorkMode;

@Data
public class EmployeeAtClientResponse {

	private String clientSalary;

	private String POSdate;// purchase order start date

	private String POEdate;// purchase order end date

	private String desgAtClient;

	
	private String POValue;

	private String PONumber;
	private String IGST;
	private String CGST;
	private String SGST;
	private String totalTax;

	private String PODate;

	private String skillSet;
	private String offerReleaseDate;
	private String clientLastWorkingDate;
	private String lancesoftLastWorkingDate;
	private String clientJoiningDate;
	private String clientLocation;

	private WorkMode workMode;
	// ---------------------
	

	private String clientEmail;

	private String clientManagerName;

	private String employee;

	private String towerHead;

	private String towerLead;

	private String recruiter;

	private String clients;
	private String subContractor;
	
	private String totalEarningAtClient;
	
	private String clientTenure;
}
