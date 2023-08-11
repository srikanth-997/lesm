package ls.lesm.service;

import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

import ls.lesm.model.User;
import ls.lesm.model.UserRole;

public interface UserService {
	
	//UMER
	public User createUser(User user, Set<UserRole> userRole) throws Exception;
	
	
	

}
