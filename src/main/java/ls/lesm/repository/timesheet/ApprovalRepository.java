package ls.lesm.repository.timesheet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.timesheet.Approval;
import ls.lesm.model.timesheet.ShiftType;
import ls.lesm.model.timesheet.TimeSheetEntry;

public interface ApprovalRepository extends JpaRepository<Approval, Integer> {

	Optional<Approval> findByTimeSheetEntry(TimeSheetEntry entry);

}
