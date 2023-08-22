package ls.lesm.payload.request;

import java.time.LocalDate;

import lombok.Data;
@Data
public class UpdateClientDetialsDropDown {
	
	private Integer clientId;
	private String clientName;
	private LocalDate poSDate;
	private LocalDate poEDate;

}
