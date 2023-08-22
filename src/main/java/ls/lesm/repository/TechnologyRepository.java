package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.Technology;

public interface TechnologyRepository extends JpaRepository<Technology, Integer> {

	Optional<Technology> findByTechnologyIgnoreCase(String technology);

}
