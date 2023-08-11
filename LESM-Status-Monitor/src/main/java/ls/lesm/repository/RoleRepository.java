package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.Role;
import ls.lesm.model.User;
import ls.lesm.model.UserRole;
import ls.lesm.payload.request.RoleRequest;

public interface RoleRepository extends JpaRepository<Role, Integer> {



	Role findByRoleName(String roleName);

	Role findByRoleId(Integer roleId);

	void deleteByRoleName(String roleName);

	void save(RoleRequest role);

	//Optional<Role> findByUser(User u);


}
