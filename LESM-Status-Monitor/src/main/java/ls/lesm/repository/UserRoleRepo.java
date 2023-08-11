package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.User;
import ls.lesm.model.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {
	Optional<UserRole> findByUser(User user);

}
