package ls.lesm.restcontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.bos.EntryTypeBo;
import ls.lesm.bos.HolidayCalenderBo;
import ls.lesm.bos.InternalProjectBo;
import ls.lesm.bos.ModeBo;
import ls.lesm.bos.ShiftTypeBo;
import ls.lesm.model.timesheet.Approval;
import ls.lesm.payload.request.ApprovalRequest;
import ls.lesm.payload.request.TimeSheetEntryRequest;
import ls.lesm.payload.response.EmployeeLinesResponse;
import ls.lesm.payload.response.TimeSheetHistoryResponse;
import ls.lesm.service.impl.TimeSheetServiceImpl;

@RestController
@RequestMapping("/api/v1/timesheet")
@CrossOrigin("*")
public class TimeSheetController {

	@Autowired
	private TimeSheetServiceImpl<?> timeSheetService;

//	private EmployeeDetailsServiceImpl

	@PostMapping("/shift-type")
	public ResponseEntity<Integer> addShiftType(@RequestBody ShiftTypeBo shiftType) {

		return new ResponseEntity<Integer>(this.timeSheetService.saveShiftType(shiftType), HttpStatus.OK);
	}

	@GetMapping("/shift-types")
	public ResponseEntity<List<ShiftTypeBo>> findAllShiftType() {

		return new ResponseEntity<List<ShiftTypeBo>>(this.timeSheetService.getAllShiftType(), HttpStatus.OK);
	}

	@PostMapping("/mode")
	public ResponseEntity<Integer> addMode(@RequestBody ModeBo modeBo) {

		return new ResponseEntity<Integer>(this.timeSheetService.saveMode(modeBo), HttpStatus.OK);
	}

	@GetMapping("/modes")
	public ResponseEntity<List<ModeBo>> findAllMode() {
		return new ResponseEntity<List<ModeBo>>(this.timeSheetService.getAllModes(), HttpStatus.OK);
	}

	@PostMapping("/project")
	public ResponseEntity<Integer> addInternalProject(@RequestBody InternalProjectBo internalProjecBo) {

		return new ResponseEntity<Integer>(this.timeSheetService.saveInternalProject(internalProjecBo), HttpStatus.OK);
	}
	
	@PostMapping("/holiday")
	public ResponseEntity<?> addHoliday(@RequestBody HolidayCalenderBo holidayCalenderBo) {
		this.timeSheetService.saveHoliday(holidayCalenderBo);
		return new ResponseEntity<>( HttpStatus.CREATED);
	}

	@GetMapping("/projects")
	public ResponseEntity<List<InternalProjectBo>> findAllInternalProjects() {
		return new ResponseEntity<List<InternalProjectBo>>(timeSheetService.getAllInternalProject(), HttpStatus.OK);

	}

	@PostMapping("/entry-type")
	public ResponseEntity<Integer> addEntryType(@RequestBody EntryTypeBo odTypeBo) {

		return new ResponseEntity<Integer>(timeSheetService.saveEntryType(odTypeBo), HttpStatus.OK);
	}

	@GetMapping("/entry-types")
	public ResponseEntity<List<EntryTypeBo>> findAllEntryTypes() {

		return new ResponseEntity<List<EntryTypeBo>>(timeSheetService.getAllEntryType(), HttpStatus.OK);
	}
	
	@PostMapping("/od")
	public void applyOnDuty(@RequestBody List<TimeSheetEntryRequest> req, Principal principal){
		
		timeSheetService.saveOD(req, principal);
	}
	
	@PostMapping("/leave")
	public void applyLeave(@RequestBody List<TimeSheetEntryRequest> req, Principal principal){
		
		timeSheetService.saveLeave(req, principal);
	}
	
	@PostMapping("/over-time")
	public void applyOT(@RequestBody List<TimeSheetEntryRequest> req, Principal principal){
		
		timeSheetService.saveExtraWork(req, principal);
	}
	
	@GetMapping("/history")
	public ResponseEntity<List<TimeSheetHistoryResponse>> findTimesheetByEmployee(Principal principal, 
			@RequestParam(required = false) Integer month,
			@RequestParam(required = false) Integer entryType){
		
		return new ResponseEntity<List<TimeSheetHistoryResponse>>( timeSheetService.getTimeSheetHistory( principal,month,entryType),HttpStatus.OK);
	}
	
	@PostMapping("/approve")
	public ResponseEntity<?> approveTimeSheetEntries(@RequestBody List<ApprovalRequest> req){
	
		return new ResponseEntity<>( timeSheetService.approveEntries(req),HttpStatus.OK);
	}
	

	@GetMapping("/requests")
	public ResponseEntity<List<EmployeeLinesResponse>> findAllRepoteesTimeSheetRequest(Principal principal){
	
		return new ResponseEntity<List<EmployeeLinesResponse>>( timeSheetService.getAllRepoteesTimeSheetRequest(principal),HttpStatus.OK);
	}
	
	
}
