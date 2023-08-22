package ls.lesm.repository.timesheet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.timesheet.TimeSheetEntry;

public interface TimeSheetEntryRepository extends JpaRepository<TimeSheetEntry, Integer> {

	

List<TimeSheetEntry> findByEmployeeId(MasterEmployeeDetails employee);

}
