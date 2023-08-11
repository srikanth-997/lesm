package ls.lesm.service.impl;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ls.lesm.bos.EntryTypeBo;
import ls.lesm.bos.HolidayCalenderBo;
import ls.lesm.bos.ModeBo;
import ls.lesm.bos.ShiftTypeBo;
import ls.lesm.bos.TimeSheetEntryBo;
import ls.lesm.constants.EntryTypeConstant;
import ls.lesm.exception.SupervisorAlreadyExistException;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.exp.ExpenseNotification;
import ls.lesm.model.recruiter.Status;
import ls.lesm.model.timesheet.Approval;
import ls.lesm.model.timesheet.EmployeeLeaveBalance;
import ls.lesm.model.timesheet.EntryType;
import ls.lesm.model.timesheet.HolidayCalender;
import ls.lesm.model.timesheet.Mode;
import ls.lesm.model.timesheet.ShiftType;
import ls.lesm.model.timesheet.TimeSheetEntry;
import ls.lesm.payload.request.ApprovalRequest;
import ls.lesm.payload.request.TimeSheetEntryRequest;
import ls.lesm.payload.response.EmployeeLinesResponse;
import ls.lesm.payload.response.TimeSheetHistoryResponse;
import ls.lesm.repository.InternalProjectRepository;
import ls.lesm.repository.expRepo.ExpenseNotificatonRepo;
import ls.lesm.repository.timesheet.ApprovalRepository;
import ls.lesm.repository.timesheet.EmployeeLeaveBalanceRepository;
import ls.lesm.repository.timesheet.EntryTypeRepository;
import ls.lesm.repository.timesheet.HolidayCalenderRepository;
import ls.lesm.repository.timesheet.ModeRepository;
import ls.lesm.repository.timesheet.ShiftTypeRepository;
import ls.lesm.repository.timesheet.TimeSheetEntryRepository;

@Service
@Transactional
public class TimeSheetServiceImpl<T> extends EmployeeDetailsServiceImpl{
	
	@Autowired
	private ApprovalRepository approvalRepository;
	@Autowired
	private ModeRepository modeRepository;
	@Autowired
	private ShiftTypeRepository shiftTypeRepository;
	@Autowired
	private TimeSheetEntryRepository timeSheetEntryRepository;
	@Autowired
	private InternalProjectRepository internalProjectRepository;
	@Autowired
	private EntryTypeRepository entryTypeRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ExpenseNotificatonRepo expenseNotificatonRepo;
	
	@Autowired
	private EmployeeLeaveBalanceRepository employeeLeaveBalanceRepository;
	
	@Autowired
	private HolidayCalenderRepository holidayCalenderRepository;
	
	public ShiftTypeBo entity2BoShiftType(ShiftType shiftType) {

		return modelMapper.map(shiftType, ShiftTypeBo.class);
	}

	public ShiftType bO2EntityShiftType(ShiftTypeBo bo) {

		return modelMapper.map(bo, ShiftType.class);
	}

	public ModeBo entity2BoModeBo(Mode mode) {

		return modelMapper.map(mode, ModeBo.class);
	}

	public Mode bO2EntityMode(ModeBo bo) {

		return modelMapper.map(bo, Mode.class);
	}

	public EntryTypeBo entity2EntryTypeBo(EntryType entryType) {

		return modelMapper.map(entryType, EntryTypeBo.class);
	}

	public EntryType bO2EntityEntryType(EntryTypeBo bo) {

		return modelMapper.map(bo, EntryType.class);
	}
	
	public TimeSheetEntry bo2EntityTimeSheetEntry(TimeSheetEntryBo timeSheetEntryBo) {
		
		return modelMapper.map(timeSheetEntryBo, TimeSheetEntry.class);
	}
	
	public TimeSheetEntryBo entity2BoTimeSheetEntry(TimeSheetEntry timeSheetEntry) {
		
		return modelMapper.map(timeSheetEntry, TimeSheetEntryBo.class);
	}
	
      public HolidayCalenderBo entity2BoHolidayCalenderBo(HolidayCalender holiDayCalender) {
		
		return modelMapper.map(holiDayCalender, HolidayCalenderBo.class);
	}
      
     public HolidayCalender bo2EntityHolidayCalender(HolidayCalenderBo holidayCalenderBo) {
    	 
    	 return modelMapper.map(holidayCalenderBo, HolidayCalender.class);
     }
	
	public ExpenseNotification createNotification(String message, MasterEmployeeDetails employee) {
		ExpenseNotification noti = new ExpenseNotification();
		noti.setCreatedAt(LocalDateTime.now());
		noti.setFlag(true);
		noti.setMessage(message);
		noti.setMasterEmployeeDetails(employee);
		
		return expenseNotificatonRepo.save(noti);

	}
	
	public void saveHoliday(HolidayCalenderBo bo) {
		
		Optional<HolidayCalender> holiday=holidayCalenderRepository.findByHolidayDate(bo.getHolidayDate());
		if(holiday.isPresent()) {
			throw new SupervisorAlreadyExistException("To this date holiday aready added to calender");
		}
		
		bo.setCreatedAt(LocalDateTime.now());
		holidayCalenderRepository.save(bo2EntityHolidayCalender(bo));
	}
	
	public HolidayCalenderBo getHolidayByDate(LocalDate date) {
		Optional<HolidayCalender> holiday=	 holidayCalenderRepository.findByHolidayDate(date);
		if(holiday.isPresent())
		return entity2BoHolidayCalenderBo(holiday.get());
		else
			return null;
	
	}

	public Integer saveShiftType(ShiftTypeBo shiftTypeBo) {

		shiftTypeBo.setCreatedAt(LocalDateTime.now());
	
	 Optional<ShiftType> optShiftType =this.shiftTypeRepository.findByShiftCodeIgnoreCase(shiftTypeBo.getShiftCode());
	 
	 if(optShiftType.isPresent())
		 throw new SupervisorAlreadyExistException("This "+optShiftType.get().getShiftCode()+" is already present");
	 
	 	
		return this.shiftTypeRepository.save(bO2EntityShiftType(shiftTypeBo)).getId();
	}
	
	public List<ShiftTypeBo> getAllShiftType() {
		List<ShiftType> shiftTypes = this.shiftTypeRepository.findAll();
		return shiftTypes.stream().map(this::entity2BoShiftType).collect(Collectors.toList());
	}
	
	
	public Integer saveMode(ModeBo modeBo) {

		modeBo.setCreatedAt(LocalDateTime.now());
		Optional<Mode> optMode = this.modeRepository.findByModeIgnoreCase(modeBo.getMode());

		if (optMode.isPresent())
			throw new SupervisorAlreadyExistException("This mode " + optMode.get().getMode() + " is already present");

		return modeRepository.save(bO2EntityMode(modeBo)).getId();

	}
	
	public List<ModeBo> getAllModes() {
		List<Mode> modes = this.modeRepository.findAll();
		return modes.stream().map(this::entity2BoModeBo).collect(Collectors.toList());
	}

	public Integer saveEntryType(EntryTypeBo bo) {
		bo.setCreatedAt(LocalDateTime.now());

		Optional<EntryType> entryType =entryTypeRepository.findByEntryTypeIgnoreCase(bo.getEntryType());
		if (entryType.isPresent())
			throw new SupervisorAlreadyExistException("This OD Type " + bo.getEntryType() + " already present");

		return entryTypeRepository.save(bO2EntityEntryType(bo)).getId();
	}

	public List<EntryTypeBo> getAllEntryType() {

		List<EntryType> odTypes = entryTypeRepository.findAll();

		return odTypes.stream().map(this::entity2EntryTypeBo).collect(Collectors.toList());
	}
	
	public EntryType getEntryTypeById(Integer id) {
		
		return entryTypeRepository.findById(id).orElseThrow();
	}
	
	public Mode getModeById(Integer id) {
		return modeRepository.findById(id).orElseThrow();
	}
	
	public ShiftType getShiftTypeById(Integer id) {
		
		return shiftTypeRepository.findById(id).orElseThrow();
	}
	
	public List<TimeSheetEntry> getAllEntriesByEmployeeId(MasterEmployeeDetails employee){
		
		return  timeSheetEntryRepository.findByEmployeeId(employee);
	}
	

	
	public void saveOD(List<TimeSheetEntryRequest> req, Principal principal) {
	    MasterEmployeeDetails employee = findEmployeeByLancesoftId(principal.getName());
	    Integer id = employee.getEmpId();

	    List<TimeSheetEntry> timeSheetEntries = getAllEntriesByEmployeeId(employee);

	    List<TimeSheetEntry> filteredTimeSheetEntries = filterRejectedEntries(timeSheetEntries);

	    List<LocalDate> existingDates = getExistingDates(filteredTimeSheetEntries);

	    List<TimeSheetEntryRequest> entries = req.stream().filter(reqLine -> {
	        LocalDate odDate = reqLine.getApplyDate();
	        validateFutureDate(odDate);
	        validateDuplicateDate(odDate, existingDates, filteredTimeSheetEntries);
	        boolean isWeekend = isWeekend(odDate);
	        boolean isHoliday = isHoliday(odDate);
	        if (isWeekend) {
	            // not saving week off
	            // reqLine.setEntryTypeId(Integer.valueOf(EntryTypeConstant.WO));
	        	//will not save this entry
	            return false;
	       
	        } 
	        if(isHoliday) { 
	        	//will not save this entry
	        	return false;
	        	
	        }else {
	            setEntryTypeId(reqLine, EntryTypeConstant.OD);
	            existingDates.add(odDate);
	            return true;
	        }
	    }).collect(Collectors.toList());

	    saveEntries(entries, employee, filteredTimeSheetEntries);
	}
	
	public void saveLeave(List<TimeSheetEntryRequest> req, Principal principal) {
		 MasterEmployeeDetails employee = findEmployeeByLancesoftId(principal.getName());
		    Integer id = employee.getEmpId();

		    List<TimeSheetEntry> timeSheetEntries = getAllEntriesByEmployeeId(employee);

		    List<TimeSheetEntry> filteredTimeSheetEntries = filterRejectedEntries(timeSheetEntries);

		    List<LocalDate> existingDates = getExistingDates(filteredTimeSheetEntries); 
		    
		    validateAndUpdateLeaveBalace(req,employee);
		    
		    List<TimeSheetEntryRequest> entries = req.stream().filter(reqLine -> {
		        LocalDate odDate = reqLine.getApplyDate();
		        validateFutureDate(odDate);
		        validateDuplicateDate(odDate, existingDates, filteredTimeSheetEntries);
		        boolean isWeekend = isWeekend(odDate);
		        boolean isHoliday = isHoliday(odDate);
		        if (isWeekend) {
		            // not saving week off
		            // reqLine.setEntryTypeId(Integer.valueOf(EntryTypeConstant.WO));
		            return false;
		        } 
		        if(isHoliday) { 
		        	//will not save this entry
		        	return false;
		        	
		        }else {
		            setEntryTypeId(reqLine, reqLine.getEntryTypeId());
		            existingDates.add(odDate);
		            return true;
		        }
		    }).collect(Collectors.toList());

		    saveEntries(entries, employee, filteredTimeSheetEntries);

	}
	
	public EmployeeLeaveBalance getLeaveBalanceByEmployeeAndLeaveType(EntryType entryType,MasterEmployeeDetails employee) {
		
		Optional<EmployeeLeaveBalance> balance= this.employeeLeaveBalanceRepository.findByLeaveTypeAndEmployee(entryType,employee);
		if(balance.isPresent())
			return balance.get();
		else 
			throw new SupervisorAlreadyExistException("EmployeeNotFound");
	}

	private void validateAndUpdateLeaveBalace(List<TimeSheetEntryRequest> reqs, MasterEmployeeDetails employee) {
	
//		AtomicInteger clCount = new AtomicInteger(0);
//		AtomicInteger elCount = new AtomicInteger(0);
//		AtomicInteger slCount = new AtomicInteger(0);
//
//		reqs.stream().forEach(req -> {
//		    if (req.getEntryTypeId().equals(Integer.valueOf(EntryTypeConstant.EL))) {
//		        elCount.incrementAndGet();
//		    }
//		    if (req.getEntryTypeId().equals(Integer.valueOf(EntryTypeConstant.CL))) {
//		        clCount.incrementAndGet();
//		    }
//		    if (req.getEntryTypeId().equals(Integer.valueOf(EntryTypeConstant.SL))) {
//		        slCount.incrementAndGet();
//		    }
//		 
//		});
		
		Long elCount = reqs.stream().filter(req -> req.getEntryTypeId().equals(Integer.valueOf(EntryTypeConstant.EL))).count();
	    Long clCount = reqs.stream().filter(req -> req.getEntryTypeId().equals(Integer.valueOf(EntryTypeConstant.CL))).count();
	    Long slCount = reqs.stream().filter(req -> req.getEntryTypeId().equals(Integer.valueOf(EntryTypeConstant.SL))).count();

		
		if (clCount.intValue() > 0) {
			EmployeeLeaveBalance clBalance = getLeaveBalanceByEmployeeAndLeaveType(getEntryTypeById(EntryTypeConstant.CL), employee);
			if (clBalance.getBalance() >= clCount.intValue()) {
				clBalance.setBalance(clBalance.getBalance() - clCount.intValue());
				clBalance.setUsed(clCount.intValue());
				clBalance.setUpdatedAt(LocalDateTime.now());
				employeeLeaveBalanceRepository.save(clBalance);
			} else {
				throw new SupervisorAlreadyExistException(
						"Your request for " + clCount + " days of Casual Leave cannot be processed as you only "
								+ clBalance.getBalance() + " Casual Leaves.");
			}
		}
		if(slCount.intValue() > 0) {
			EmployeeLeaveBalance slBalance = getLeaveBalanceByEmployeeAndLeaveType(getEntryTypeById(EntryTypeConstant.SL), employee);
			if (slBalance.getBalance() >= slCount.intValue()) {
				slBalance.setBalance(slBalance.getBalance() - slCount.intValue());
				slBalance.setUsed(slCount.intValue());
				slBalance.setUpdatedAt(LocalDateTime.now());
				employeeLeaveBalanceRepository.save(slBalance);
			} else {
				throw new SupervisorAlreadyExistException(
						"Your request for " + slCount + " days of Sick Leave cannot be processed as you only "
								+ slBalance.getBalance() + " Sick Leaves.");
			}
		}
		
		if(elCount.intValue()>0) {
			EmployeeLeaveBalance elBalance = getLeaveBalanceByEmployeeAndLeaveType(getEntryTypeById(EntryTypeConstant.EL), employee);
			if (elBalance.getBalance() >= elCount.intValue()) {
				elBalance.setBalance(elBalance.getBalance() - elCount.intValue());
				elBalance.setUsed(elCount.intValue());
				elBalance.setUpdatedAt(LocalDateTime.now());
				employeeLeaveBalanceRepository.save(elBalance);
			} else {
				throw new SupervisorAlreadyExistException(
						"Your request for " + elCount + " days of Earned Leave cannot be processed as you only "
								+ elBalance.getBalance() + " Earned Leaves.");
			}
		}
		
	}
	
	
	public void saveExtraWork(List<TimeSheetEntryRequest> req, Principal principal) {
		 MasterEmployeeDetails employee = findEmployeeByLancesoftId(principal.getName());
		    Integer id = employee.getEmpId();

		    List<TimeSheetEntry> timeSheetEntries = getAllEntriesByEmployeeId(employee);

		    List<TimeSheetEntry> filteredTimeSheetEntries = filterRejectedEntries(timeSheetEntries);

		    List<LocalDate> existingDates = getExistingDates(filteredTimeSheetEntries);
		    List<TimeSheetEntryRequest> entries = req.stream().filter(reqLine -> {
		        LocalDate odDate = reqLine.getApplyDate();
		        validateFutureDate(odDate);
		        validateDuplicateDate(odDate, existingDates, filteredTimeSheetEntries);
		        boolean isWeekend = isWeekend(odDate);
		        boolean isHoliday = isHoliday(odDate);
		        if (isWeekend || isHoliday) {
		        	
		        	setEntryTypeId(reqLine, EntryTypeConstant.OT);
		            existingDates.add(odDate);
		            
		            return true;
		        }
		        else {
		            
		            return false;
		        }
		    }).collect(Collectors.toList());

		    saveEntries(entries, employee, filteredTimeSheetEntries);

	}
	private List<TimeSheetEntry> filterRejectedEntries(List<TimeSheetEntry> entries) {
	    return entries.stream()
	            .filter(entry -> !entry.getApprovalId().getApprovalStatus().equals(Status.REJECT))
	            .collect(Collectors.toList());
	}

	private List<LocalDate> getExistingDates(List<TimeSheetEntry> entries) {
	    return entries.stream()
	            .map(TimeSheetEntry::getOdDate)
	            .collect(Collectors.toList());
	}

	private void validateFutureDate(LocalDate date) {
	    if (date.isAfter(LocalDate.now())) {
	        throw new SupervisorAlreadyExistException("Invalid: Future dates not allowed");
	    }
	}
	
	

	private void validateDuplicateDate(LocalDate date, List<LocalDate> existingDates, List<TimeSheetEntry> filteredEntries) {
	    if (existingDates.contains(date)) {
	        List<TimeSheetEntry> appliedDateRecords = filteredEntries.stream()
	                .filter(entry -> entry.getOdDate().equals(date)).collect(Collectors.toList());

	        if (appliedDateRecords.size() == 2) {
	            TimeSheetEntry firstEntry = appliedDateRecords.get(0);
	            TimeSheetEntry secondEntry = appliedDateRecords.get(1);

	            throw new SupervisorAlreadyExistException("You have already applied " +
	                    firstEntry.getEntryTypeId().getEntryType() + " on " + firstEntry.getOdDate() +
	                    " and " + secondEntry.getEntryTypeId().getEntryType() + " on " + secondEntry.getOdDate());
	        } else if (!appliedDateRecords.isEmpty()) {
	            TimeSheetEntry firstEntry = appliedDateRecords.get(0);
	            throw new SupervisorAlreadyExistException("You have already applied " +
	                    firstEntry.getEntryTypeId().getEntryType() + " on " + firstEntry.getOdDate());
	        }
	    }
	}

	private boolean isWeekend(LocalDate date) {
	    return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
	}
	
	private boolean isHoliday(LocalDate date) {
		Optional<HolidayCalender> holiday=holidayCalenderRepository.findByHolidayDate(date);
		if(holiday.isPresent())
			return true;
		else
			return false;
	}

	private void setEntryTypeId(TimeSheetEntryRequest reqLine, Integer entryTypeId) {
	    reqLine.setEntryTypeId(entryTypeId);
	}

	private void saveEntries(List<TimeSheetEntryRequest> entries, MasterEmployeeDetails employee,
			List<TimeSheetEntry> filteredEntries) {
		List<Approval> approvalList = new ArrayList<Approval>();
		entries.stream().map(reqLine -> mapToTimeSheetEntry(reqLine, employee)).forEach(entry -> {
			timeSheetEntryRepository.save(entry);
			Approval approval = approvalRepository.save(mapToApproval(employee, entry));
			approvalList.add(approval);

		});
		if(!approvalList.isEmpty()) {
		String message = createTimeSheetApplicationMessage(approvalList, employee.getSupervisor(),employee,"applied");
		createNotification(message, employee.getSupervisor());
		}
	}

	public String  getDeivceName(Device device) {
		 if (device.isMobile()) {
	            return "Mobile";
	        } else if (device.isTablet()) {
	            return "Tablet";
	        } else {
	            return "Desktop/Laptop";
	        }
	}


	private TimeSheetEntry mapToTimeSheetEntry(TimeSheetEntryRequest reqLine,
	        MasterEmployeeDetails employee) {
	    TimeSheetEntry entry = new TimeSheetEntry();
	    
	    // Get the current HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // Use the HttpServletRequest to create a Device object
        Device device = DeviceUtils.getCurrentDevice(request);
	  
	    entry.setCreatedAt(LocalDateTime.now());
	    entry.setDevice(getDeivceName(device));
	    entry.setCreatedLoginId(employee.getEmpId());
	    entry.setEmployeeId(employee);
	    entry.setEntryTypeId(getEntryTypeById(reqLine.getEntryTypeId()));
	    if(reqLine.getEmployeesAtClientsId()!=null)
	    entry.setEmployeesAtClientsId(findClientDetailsById(reqLine.getEmployeesAtClientsId()));
	    entry.setInternal(reqLine.isInternal());
	    entry.setModeId(getModeById(reqLine.getModeId()));
	    entry.setOdDate(reqLine.getApplyDate());
	    if(reqLine.getProjectId()!=null)
	    entry.setProjectId(getProjectById(reqLine.getProjectId()));
	    entry.setReason(reqLine.getReason());
	    entry.setShiftTypeId(getShiftTypeById(reqLine.getShiftTypeId()));
	    
	    return entry;
	}
	
	private Approval mapToApproval(MasterEmployeeDetails employee,TimeSheetEntry entry) {
		Approval approval=new Approval();
		approval.setApprovalStatus(Status.PENDING);
		approval.setCreatedAt(LocalDateTime.now());
		approval.setCreatedLoginId(employee.getEmpId());
		approval.setManagerId(employee.getSupervisor());
		approval.setTimeSheetEntry(entry);
		return approval;
		
	}
	
	public List<TimeSheetEntry> getTimeSheetHistroyByEmployee(MasterEmployeeDetails employee){
		return timeSheetEntryRepository.findByEmployeeId(employee);
	}
	
	
	public List<TimeSheetHistoryResponse> getTimeSheetHistory(Principal principal, Integer month, Integer entryType) {
		MasterEmployeeDetails employee = findEmployeeByLancesoftId(principal.getName());

		List<TimeSheetEntry> entries = getTimeSheetHistroyByEmployee(employee);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MMMM.yyyy hh:mm:ss a", Locale.ENGLISH);
		DateTimeFormatter dtfOnlyDate = DateTimeFormatter.ofPattern("dd.MMMM.yyyy", Locale.ENGLISH);

		int currentMonth; // Declare the variable outside of the if block
		if (month == null) {
			// Get current month if month is not provided
			LocalDate currentDate = LocalDate.now();
			currentMonth = currentDate.getMonthValue();
		} else {
			currentMonth = month;
		}

		return entries.stream()
				.filter(entry -> entry.getOdDate().getMonthValue() == currentMonth) // Filter by month	
				.filter(entry -> entryType==null || entry.getEntryTypeId().getId() == entryType)
				.map(entry -> {

					return mapToTimeSheetHistoryResponse(entry);
				}).collect(Collectors.toList());
	}

	private TimeSheetHistoryResponse mapToTimeSheetHistoryResponse(TimeSheetEntry entry) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MMMM.yyyy hh:mm:ss a", Locale.ENGLISH);
		DateTimeFormatter dtfOnlyDate = DateTimeFormatter.ofPattern("dd.MMMM.yyyy", Locale.ENGLISH);
		TimeSheetHistoryResponse res = new TimeSheetHistoryResponse();
		if (entry.getApprovalId().getApprovedAt() != null)
			res.setApproveAt(dtf.format(entry.getApprovalId().getApprovedAt()));
		res.setApproveStatus(entry.getApprovalId().getApprovalStatus());
		res.setComment(entry.getApprovalId().getComment());
		if (entry.getEmployeesAtClientsId() != null) {
			res.setClientName(entry.getEmployeesAtClientsId().getClients().getClientsNames());
			res.setEmployeeAtClientDetailId(entry.getEmployeesAtClientsId().getEmpAtClientId());
		}
		res.setDevice(entry.getDevice());
		res.setCreatedAt(entry.getCreatedAt());
		res.setCreatedLoginId(entry.getCreatedLoginId());
		res.setEmployeeId(entry.getEmployeeId().getEmpId());
		res.setEmployeeName(StringUtils.capitalize(
				entry.getEmployeeId().getFirstName() + " " + entry.getEmployeeId().getLastName()));
		res.setId(entry.getId());
		res.setEntryTypeId(entry.getEntryTypeId());
		res.setManagerId(entry.getApprovalId().getManagerId().getEmpId());
		res.setManagerName(StringUtils.capitalize(entry.getApprovalId().getManagerId().getFirstName() + " "
				+ entry.getApprovalId().getManagerId().getLastName()));

		res.setInternal(entry.isInternal());
		res.setModeId(entry.getModeId());
		res.setOdDate(dtfOnlyDate.format(entry.getOdDate()));
		res.setProjectId(entry.getProjectId());
		res.setReason(entry.getReason());
		res.setShiftTypeId(entry.getShiftTypeId());
		return res;
	}

	
	private Approval getApprovalEntryByTimesheetEntryId(TimeSheetEntry entry) {
		return this.approvalRepository.findByTimeSheetEntry(entry).get();
	}
	
	private TimeSheetEntry getTimesheetById(Integer id) {
		return this.timeSheetEntryRepository.findById(id).get();
	}
	
	public HttpStatus approveEntries(List<ApprovalRequest> requests) {
		
	List<Approval> approvalList= requests.stream().map(req -> { 
			Approval approval= getApprovalEntryByTimesheetEntryId(getTimesheetById(req.getId()));
			approval.setApprovedAt(LocalDateTime.now());
			approval.setApprovalStatus(req.getStatus());
			approval.setComment(req.getComment());
			return approvalRepository.save(approval);
	
		}).collect(Collectors.toList());
	MasterEmployeeDetails employee=approvalList.get(0).getTimeSheetEntry().getEmployeeId();
	
	createNotification(createTimeSheetApplicationMessage(approvalList,employee,employee.getSupervisor(),"approve"), employee);
	 
	 return HttpStatus.OK;
	}
	
	
	private String createTimeSheetApplicationMessage(List<Approval> approvalList, MasterEmployeeDetails notifiedEmployee, MasterEmployeeDetails fromNotification,String applyingTypeMsg) {
	    List<LocalDate> dateList = approvalList.stream()
	            .filter(approval -> approval.getTimeSheetEntry() != null)
	            .map(approval -> approval.getTimeSheetEntry().getOdDate())
	            .collect(Collectors.toList());

	    String toEmployee = StringUtils.capitalize(notifiedEmployee.getFirstName() + " " + notifiedEmployee.getLastName());
	    String fromEmployee = StringUtils.capitalize(fromNotification.getFirstName() + " " + fromNotification.getLastName());
	    
	    String entryType = "";
	    if(approvalList.get(0).getTimeSheetEntry().getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.CL)) ||
	       approvalList.get(0).getTimeSheetEntry().getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.EL)) ||
	       approvalList.get(0).getTimeSheetEntry().getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.SL)) ||
	       approvalList.get(0).getTimeSheetEntry().getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.LOP))) {
	    	
	    	 entryType = "Leave";
	   
	    }else
	    entryType = approvalList.get(0).getTimeSheetEntry().getEntryTypeId().getEntryType();
	    
	    String message = "";
	    String messageType=applyingTypeMsg;

	    if (dateList.isEmpty()) {
	        // no dates in the list
	        message = "Hi " + toEmployee + ", " + fromEmployee + " has not "+messageType+" for any " + entryType + ".";
	    } else {
	        // at least one date in the list
	        List<LocalDate> startDates = new ArrayList<>();
	        List<LocalDate> endDates = new ArrayList<>();

	        LocalDate startDate = null;
	        LocalDate endDate = null;

	        for (LocalDate date : dateList) {
	            if (endDate == null || date.isAfter(endDate.plusDays(1))) {
	                // this is a new range, so add the previous range's start and end dates
	                if (startDate != null) {
	                    startDates.add(startDate);
	                    endDates.add(endDate);
	                }
	                // set the new range's start date
	                startDate = date;
	            }
	            // set the new range's end date
	            endDate = date;
	        }

	        // don't forget to add the last range's start and end dates
	        startDates.add(startDate);
	        endDates.add(endDate);

	        // build the message for each date range
	        StringBuilder dateRangesMessage = new StringBuilder();
	        for (int i = 0; i < startDates.size(); i++) {
	            if (i > 0) {
	                dateRangesMessage.append(" and ");
	            }
	            if (startDates.get(i).equals(endDates.get(i))) {
	                // this is a single-date range
	                dateRangesMessage.append("on ").append(startDates.get(i));
	            } else {
	                // this is a multi-date range
	                dateRangesMessage.append("from ").append(startDates.get(i)).append(" to ").append(endDates.get(i));
	            }
	        }

	        message = "Hi " + toEmployee + ", " + fromEmployee + " has "+messageType+" for " + entryType + " " + dateRangesMessage.toString() + ".";
	    }

	    return message;
	}
	
	

	public List<EmployeeLinesResponse> getAllRepoteesTimeSheetRequest(Principal principal) {

		MasterEmployeeDetails manager = findEmployeeByLancesoftId(principal.getName());
		List<MasterEmployeeDetails> repotees = this.masterEmployeeDetailsRepository.findBySupervisor(manager);

		return repotees.stream().map(repotee -> {
			EmployeeLinesResponse res = new EmployeeLinesResponse();
			res.setEmployeeName(StringUtils.capitalize(repotee.getFirstName() + " " + repotee.getLastName()));
			res.setId(repotee.getEmpId());
			res.setLancesoftId(repotee.getLancesoft());
			List<TimeSheetEntry> pendingEntries = getAllEntriesByEmployeeId(repotee).stream()
					.filter(entry -> entry.getApprovalId().getApprovalStatus().equals(Status.PENDING))
					.collect(Collectors.toList());
			res.setAllRequest(pendingEntries.size());

			List<TimeSheetHistoryResponse> odEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.OD)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			List<TimeSheetHistoryResponse> extraEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.OT)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			List<TimeSheetHistoryResponse> elEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.EL)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			List<TimeSheetHistoryResponse> clEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.CL)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			List<TimeSheetHistoryResponse> hodEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.HOD)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			List<TimeSheetHistoryResponse> slEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.SL)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			List<TimeSheetHistoryResponse> phEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.PH)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			

			List<TimeSheetHistoryResponse> lopEntries = pendingEntries.stream().filter(
					pendingEntry -> pendingEntry.getEntryTypeId().equals(getEntryTypeById(EntryTypeConstant.LOP)))
					.map(this::mapToTimeSheetHistoryResponse).collect(Collectors.toList());
			
			res.setOdRequest(odEntries.size());
			res.setClRequest(clEntries.size());
			res.setElRequest(elEntries.size());
			res.setExtraWorkRequst(extraEntries.size());
			res.setHodRequest(hodEntries.size());
			res.setOdRequest(odEntries.size());
			res.setPhRequest(phEntries.size());
			res.setSlRequest(slEntries.size());
			res.setLopRequest(lopEntries.size());
			if (repotee.getEmployeePhoto() != null)
				res.setEmployeePhoto(repotee.getEmployeePhoto().getProfilePic());
			res.setOds(odEntries);
			res.setExtraWork(extraEntries);
			res.setHod(hodEntries);
			res.setPh(phEntries);
			Map<String, List<TimeSheetHistoryResponse>> leaveRes = new HashMap<>();
			leaveRes.put("SL", slEntries);
			leaveRes.put("EL", elEntries);
			leaveRes.put("CL", clEntries);
			leaveRes.put("LOP", lopEntries);
			res.setLeaves(leaveRes);
			return res;
		}).collect(Collectors.toList());
	}
	
}