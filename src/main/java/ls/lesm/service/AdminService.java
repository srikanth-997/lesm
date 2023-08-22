package ls.lesm.service;

import java.util.List;

import ls.lesm.model.Role;
import ls.lesm.payload.request.RoleRequest;

public interface AdminService {
	//UMER
	public void deleteRoles(String roleName);
	//UMER
	public Role createNewRole(Role role);
	//UMER
	public List<Role> getAllRole();

}
