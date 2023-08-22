package ls.lesm.repository.timesheet;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.timesheet.HolidayCalender;

public interface HolidayCalenderRepository extends JpaRepository<HolidayCalender, Integer> {

	Optional<HolidayCalender> findByHolidayDate(LocalDate holidayDate);

}
