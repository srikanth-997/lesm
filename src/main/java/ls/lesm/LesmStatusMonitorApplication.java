package ls.lesm;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ls.lesm.model.Role;
import ls.lesm.model.User;
import ls.lesm.model.UserRole;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.timesheet.EmployeeLeaveBalanceRepository;
import ls.lesm.service.impl.TimeSheetServiceImpl;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class LesmStatusMonitorApplication implements CommandLineRunner {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private MasterEmployeeDetailsRepository mrepo;
	@Autowired
	private EmployeeLeaveBalanceRepository lrepo;
	@Autowired
	private TimeSheetServiceImpl tSer;
	
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(LesmStatusMonitorApplication.class, args);
		
		
	}	
	
	

	
		
	
	@Override
	public void run(String... args) throws Exception{
		
		
//		//////////
//		System.out.println("execution start");
//		
//		User user=new User();
//		
//		user.setEmail("user1@gmail.com");
//		user.setFirstName("test");
//		user.setLastName("user");
//		
//		//user.setPhoneNo("7350957167");
//		user.setPassword(bCryptPasswordEncoder.encode("user"));
//		user.setUsername("LSI001");
//		
//		Role role=new Role();
//		role.setRoleId(1);
//		role.setRoleName("HR");
//		
//		Set<UserRole> userRoleSet=new HashSet<>();
//		UserRole userRole=new UserRole();
//		userRole.setRole(role);
//		userRole.setUser(user);
//		userRoleSet.add(userRole);
//		System.out.println("exc end");
		
		
		
		
		

}
}