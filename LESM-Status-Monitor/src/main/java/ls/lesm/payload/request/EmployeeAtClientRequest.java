package ls.lesm.payload.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import ls.lesm.model.enums.WorkMode;

@Data	
public class EmployeeAtClientRequest {

	private Double clientSalary;

	private LocalDate POSdate;// purchase order start date

	private LocalDate POEdate;// purchase order end date

	private String desgAtClient;

	// new fields added on 25-01-2023
	private Long POValue;

	private String PONumber;
	private Double IGST;
	private Double CGST;
	private Double SGST;

	private LocalDate PODate;

	private String skillSet;
	private LocalDate offerReleaseDate;
	private LocalDate clientLastWorkingDate;
	private LocalDate lancesoftLastWorkingDate;
	private LocalDate clientJoiningDate;
	private String clientLocation;

	private WorkMode workMode;
	// ---------------------
	@JsonIgnore
	private LocalDate createdAt;// timpStamp

	private String createdBy;// principal

	private String clientEmail;

	private String clientManagerName;

	private Integer empId;

	private Integer towerHead;

	private Integer towerLead;

	private Integer recruiter;

	private Integer clients;
	
	private Integer subContractor;
}
