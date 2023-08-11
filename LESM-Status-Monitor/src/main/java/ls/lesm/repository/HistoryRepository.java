package ls.lesm.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.History;
import ls.lesm.model.MasterEmployeeDetails;

public interface HistoryRepository extends JpaRepository<History, Integer> {

	//List<History> findByCreatedAt(LocalDate date);

	List<History> findByCreatedAt(LocalDate localDate);

    // List<History> findbyLancesoft(String lancesoft);

	List<History> findByLancesoft(String lancesoft);
	
	
	@Query("FROM History g where g.supervisor.id = :supervisor")
	List<History> findsBymasterEmployeeDetails_Id(@Param("supervisor") Integer id);

	List<History> findByUpdatedBy(MasterEmployeeDetails masterEmployeeDetails);

}

