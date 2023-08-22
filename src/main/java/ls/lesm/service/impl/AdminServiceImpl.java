
package ls.lesm.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.RoleAreadyExistException;
import ls.lesm.model.Role;
import ls.lesm.repository.RoleRepository;
import ls.lesm.service.AdminService;
@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Transactional
	@Override
	public void deleteRoles(String roleName) {
		roleRepository.deleteByRoleName(roleName);

	}

	//UMER
	@Transactional
	@Override
	public Role createNewRole(Role role) {
		Role local1=this.roleRepository.findByRoleName(role.getRoleName());
		Role local2=this.roleRepository.findByRoleId(role.getRoleId());
		if(local1!=null)
			throw new RoleAreadyExistException("111", "This Role Name is already exist");
		if(local2!=null)
			throw new RoleAreadyExistException("222", "This Role ID Is Already Exist");
		else 
			role.setRoleName(role.getRoleName().toUpperCase());
			  this.roleRepository.save(role);
			  return new Role();
	
	}

	//UMER
	@Override
	public List<Role> getAllRole() {
	List<Role> allRoles=this.roleRepository.findAll();
		return allRoles;
	}
	
	

}
