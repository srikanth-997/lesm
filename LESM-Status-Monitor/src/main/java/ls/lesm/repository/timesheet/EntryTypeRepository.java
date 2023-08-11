package ls.lesm.repository.timesheet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.timesheet.EntryType;

public interface EntryTypeRepository extends JpaRepository<EntryType, Integer> {



	Optional<EntryType> findByEntryTypeIgnoreCase(String entryType);

}
