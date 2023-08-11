package ls.lesm.repository.timesheet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.timesheet.Mode;

public interface ModeRepository extends JpaRepository<Mode, Integer> {

	Optional<Mode> findByModeIgnoreCase(String mode);

}
