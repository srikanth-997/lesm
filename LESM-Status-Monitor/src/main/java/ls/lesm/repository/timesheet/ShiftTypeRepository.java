package ls.lesm.repository.timesheet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.timesheet.ShiftType;

public interface ShiftTypeRepository extends JpaRepository<ShiftType, Integer> {

	Optional<ShiftType> findByShiftCode(String shiftCode);

	Optional<ShiftType> findByShiftCodeIgnoreCase(String shiftCode);

}
