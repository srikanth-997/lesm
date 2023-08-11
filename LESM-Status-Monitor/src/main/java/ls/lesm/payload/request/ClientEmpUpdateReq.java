package ls.lesm.payload.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClientEmpUpdateReq {
	
	private String clientManagerEmail;
	private String clientManagerName;
	private Double clientSalary;
	private String desgAtClient;
	private LocalDate poSDate;
	private LocalDate poEDate;
	private Integer clientId;
	
	private String PONumber;
	private Double IGST;
	private Double CGST;
	private Double SGST;
	private Double totalTax;
	private String handledBy;
	private String towerHead;
	private LocalDate PODate;
	private Long POValue;
	

}
