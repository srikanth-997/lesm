package ls.lesm.restcontroller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antlr.RecognitionException;
import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.User;
import ls.lesm.payload.request.JwtRequest;
import ls.lesm.payload.request.PasswordRequest;
import ls.lesm.payload.response.JwtResponse;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.securityconfig.JwtUtil;
import ls.lesm.service.impl.DeleteEmployeeService;
import ls.lesm.service.impl.UserDetailsServiceImpl;
import ls.lesm.service.impl.UserServiceImpl;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private static final Logger LOG=LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;// authenticate method will use this to authenticate 

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	private DeleteEmployeeService deleteEmployeeService;
	//UMER
	@PostMapping("/login")
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		LOG.info("Enterd into generateToken method");
		String username="";
		try {
			String numbesOnly=jwtRequest.getUsername().replaceAll("[^0-9]", "");
			String charOnly=jwtRequest.getUsername().replaceAll("[^a-z ^A-Z]", "");
			if(charOnly.equals("LSI") || charOnly.equals("lsi") || charOnly.equals(""))
			username="LSI".concat(numbesOnly);
			else
				username=charOnly.concat(charOnly);
			//System.out.println("=========================="+charOnly);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, jwtRequest.getPassword()));

		}catch(UsernameNotFoundException unfe) {
			LOG.debug("Unable to login, User Not Found{}",unfe.getMessage());
			unfe.printStackTrace();
			throw new Exception("User Not Found");
		}

		UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
		String token=this.jwtUtil.generateToken(userDetails);// this will give token
		
		String lancesoft=username;
		MasterEmployeeDetails loggedInEmployee=this.masterEmployeeDetailsRepository.findByLancesoft(lancesoft);
		User authuser=this.userRepository.findByUsername(lancesoft);
        String role=authuser.getRoleName();
        String firstName=authuser.getFirstName();	
        String lastName=authuser.getLastName();
        int empId=loggedInEmployee.getEmpId();
//      String firstName=loggedInEmployee.getFirstName();
//		String lastName=loggedInEmployee.getLastName();
		

		return ResponseEntity.ok(new JwtResponse(token,role,firstName,lastName,lancesoft,empId));
	}
	
	
	
	//UMER
	private void authenticate(String username, String password) throws Exception {// this method will auth if auth is not sucessfull then is will throegh excep.
		LOG.info("Ented Into authenticate method");
		try {

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));//takinf uname and pass wich we have set in jwtfilter class

		}catch(DisabledException de) {
			LOG.debug("User Is Disabled due to this {}",de.getMessage());
			throw new Exception("USER IS Disabled"+de.getMessage());
		}catch(BadCredentialsException be) {
			LOG.debug("Invalid Credentials due to this {}",be.getMessage());
			throw new Exception("Invalid Credentials" +be.getMessage());

		}
	}
	//UMER
	@PutMapping("/change-password")
	public ResponseEntity<String> changePwd(@RequestBody PasswordRequest pwdRequest, Principal principal){

		String LoggedInUserUsername=principal.getName();
		User currentUser=this.userRepository.findByUsername(LoggedInUserUsername);

		if(pwdRequest.getNewPassword() != null && !pwdRequest.getNewPassword().isEmpty() 
				&& !pwdRequest.getNewPassword().equals("") && !pwdRequest.getNewPassword().contains(" ")) {

			if(bCryptPasswordEncoder.matches(pwdRequest.getOldPassword(),currentUser.getPassword())){//this will check old pwd fiels with current pwd 
			currentUser.setPassword(bCryptPasswordEncoder.encode(pwdRequest.getNewPassword()));
			userRepository.save(currentUser);
			}else {
				throw new RecordNotFoundException("Incorrect old password");
			}
	  }
		return new ResponseEntity<String>("Updated ",HttpStatus.CREATED);
    }
	
//	// Teju
//		@DeleteMapping("/delete-employee/{LancesoftId}/{flag}")
//		public ResponseEntity<?> deleteEmployee(@PathVariable String LancesoftId, @PathVariable boolean flag) {
//
//			String submit_message = deleteEmployeeService.deleteEmployeeSubmit(LancesoftId, flag);
//
//			return new ResponseEntity<String>(submit_message, HttpStatus.ACCEPTED);
//
//		}
	
}