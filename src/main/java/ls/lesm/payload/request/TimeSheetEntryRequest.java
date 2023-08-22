package ls.lesm.payload.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetEntryRequest {
	
	private LocalDate applyDate;
	private boolean isInternal;
	private String reason;
	private Integer modeId;
	private Integer shiftTypeId;
	private Integer projectId;
	private Integer employeesAtClientsId;
	private Integer entryTypeId;
	

}
