package ls.lesm.payload.request;

import ls.lesm.model.timesheet.EntryType;
import ls.lesm.model.timesheet.Mode;
import ls.lesm.model.timesheet.ShiftType;

public class OnDuityRequest {
	
	private Integer id;
	private String odDate;
	private boolean isInternal;
	private String reason;
	private Mode mode;
	private EntryType  entryType;
	private ShiftType shiftType;
	

}
