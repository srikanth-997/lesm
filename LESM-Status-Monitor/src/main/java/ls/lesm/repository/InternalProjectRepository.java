
package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.InternalProject;

public interface InternalProjectRepository extends JpaRepository<InternalProject, Integer> {

	Optional<InternalProject> findByProjectTitleIgnoreCase(String projectTitle);

	

}
