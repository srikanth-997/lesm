package ls.lesm.repository.timesheet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.timesheet.EmployeeLeaveBalance;
import ls.lesm.model.timesheet.EntryType;

public interface EmployeeLeaveBalanceRepository extends JpaRepository<EmployeeLeaveBalance, Integer> {

	Optional<EmployeeLeaveBalance> findByLeaveTypeAndEmployee(EntryType entryType, MasterEmployeeDetails employee);

}
