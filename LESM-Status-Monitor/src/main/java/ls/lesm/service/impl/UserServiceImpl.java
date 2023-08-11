package ls.lesm.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.exception.UserAlreadinExistException;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.User;
import ls.lesm.model.UserRole;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.RoleRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	//UMER
	@Override
	public User createUser(User user, Set<UserRole> userRole) throws Exception {
		
		User local=this.userRepository.findByUsername(user.getUsername());
		user.setUsername(user.getUsername().toUpperCase());	
		
		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(user.getUsername());
		
		
		
	//	System.out.println("\n\n\n\n>>>>>>>>>>>>>>>>>>>>>>>hi");
		if(employee==null) {
		    throw new RecordNotFoundException("The employee with this username ~"+user.getUsername()+" is not exist!");
		}
		if(local!=null) {
			System.out.println("User already exist with this username!!");
			throw new UserAlreadinExistException("Username is invalid or already has credentials");
			//create user
		}
		else {

			for(UserRole ur: userRole) {
				roleRepository.save(ur.getRole());//role save
			}

			user.getUserRole().addAll(userRole);//associating roles to user
			local=this.userRepository.save(user);
		}
		

		return local;
		
	}

}
