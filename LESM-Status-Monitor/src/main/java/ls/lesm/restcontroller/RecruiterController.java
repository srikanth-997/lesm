package ls.lesm.restcontroller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ls.lesm.model.User;
import ls.lesm.model.recruiter.CandidateProfiles;
import ls.lesm.model.recruiter.JobString;
import ls.lesm.model.recruiter.RecruiterProfitOrLoss;
import ls.lesm.payload.request.ApproveProfilesRequest;
import ls.lesm.payload.request.InterviewScheduleStatusRequest;
import ls.lesm.payload.response.CandidateProfilesResponse;
import ls.lesm.payload.response.JobStringResponse;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.UserService;
import ls.lesm.service.impl.RecruiterServiceImpl;

@RestController
@RequestMapping("/api/v1/rec")
@CrossOrigin("*")
public class RecruiterController {

	@Autowired
	private RecruiterServiceImpl recruiterServiceImpl;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepo;

	
//	// firebase bucket
//	@PostMapping(value = "/post-jobstring", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
//			MediaType.APPLICATION_JSON_VALUE })
	@PostMapping("/post-jobstring")
	public ResponseEntity<?> postString(@RequestParam String jobString, Principal principal,
			@RequestParam Set<Integer> empIds, @RequestParam(required = false) MultipartFile file,
			@RequestParam  Integer clientId,
			@RequestParam Integer hiringTypeId)
			throws JsonMappingException, JsonProcessingException {
		/*
		 * this is sample json RequestBody to post a jobString 
		 * { 
		 * "budget": "34lpa",
		 *  "closeDate": "2022-10-10", 
		 *  "jd": "dsfaff ",
		 *  "openDate": "2022-09-28", 
		 * "ticketStatus": true,
		 *  "totalPosition": 15
		 *   }
		 */
		// taking jobString as an String bcz we cant parse json and multipart data in
		// same http request
		// so taking it as an String and here converting it into a json using model
		// mapper and passing onverted
		// value to postJobString method
		ObjectMapper objectMapper = new ObjectMapper();
		JobString jobS = objectMapper.readValue(jobString, JobString.class);

		String ticket = this.recruiterServiceImpl.postJobString(jobS, principal, empIds, file,clientId,hiringTypeId);
		return new ResponseEntity<>(ticket, HttpStatus.CREATED);
	}

	// for testing purpose
	@GetMapping("/tempp")
	public ResponseEntity<?> temp(Principal principal) {

		User user=this.userRepo.findByUsername(principal.getName());
		//user.isAccountNonLocked()
		//this.userRepo.save(user);
		return new ResponseEntity<>(user.isAccountNonExpired(),HttpStatus.ACCEPTED);
	}

	@GetMapping("/loggedin-jobstring")
	public ResponseEntity<List<JobString>> getOpendJobStringByLoggedInEmp(Principal principal) {

		return new ResponseEntity<List<JobString>>(this.recruiterServiceImpl.getOpenedJobStringByLoggedInEmp(principal),
				HttpStatus.ACCEPTED);
	}

	// firebase bucket
	@PostMapping("/send-profile")
	public ResponseEntity<String> sendProfiles(@RequestParam String profiles, Principal principal, @RequestPart MultipartFile file,
			@RequestParam Integer jobStringId)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		CandidateProfiles candidateProfiles = objectMapper.readValue(profiles, CandidateProfiles.class);

		this.recruiterServiceImpl.sendProfiles(candidateProfiles, jobStringId, principal, file);

		return new ResponseEntity<String>(HttpStatus.CREATED);

	}

	@PutMapping("/approve-profiles")
	public ResponseEntity<ApproveProfilesRequest> approveProfiles(@RequestBody List<ApproveProfilesRequest> req) {
		this.recruiterServiceImpl.managerProfileApproval(req);
		return new ResponseEntity<ApproveProfilesRequest>(HttpStatus.OK);
	}

	@GetMapping("/schedule-status")
	public ResponseEntity<InterviewScheduleStatusRequest> scheduleAndStatus (@RequestParam String candidateId,Principal principal) {

		return new ResponseEntity<InterviewScheduleStatusRequest>(
				this.recruiterServiceImpl.scheduleInterview(candidateId, principal), HttpStatus.ACCEPTED);
	}

	@GetMapping("/profit-or-loss")
	public ResponseEntity<List<RecruiterProfitOrLoss>> recruiterProfitOrLoss(Principal principal) {

		return new ResponseEntity<List<RecruiterProfitOrLoss>>(
				this.recruiterServiceImpl.getRecruiterProfitOrLoss(principal), HttpStatus.ACCEPTED);
	}

	@GetMapping("/posted-jobs")
	public ResponseEntity<List<JobStringResponse>> getPostJobByLoggedInAndStatus(Principal principal, @RequestParam boolean falg) {
         
		return new ResponseEntity<List<JobStringResponse>>(
				 this.recruiterServiceImpl.getPostedJobStringByLoggedIn(principal, falg), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/posted-jobs-filters")
	public ResponseEntity<List<JobStringResponse>> getPostJobByLoggedInAndStatus(Principal principal, @RequestParam boolean falg,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) Integer hiringType,
			@RequestParam(required = false) Integer clientId) {
     
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

         // Parse the string into a LocalDate object
         LocalDate localDate = LocalDate.parse(date, formatter);
      
         
		return new ResponseEntity<List<JobStringResponse>>(
				 this.recruiterServiceImpl.getPostedJobStringByLoggedInAndFillters(principal, falg,localDate, hiringType, clientId ), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/tagged-jobs")
	public ResponseEntity<List<JobStringResponse>> getTaggedJobByLoggedInAndStatus(Principal principal, @RequestParam boolean falg) {
         
		return new ResponseEntity<List<JobStringResponse>>(
				 this.recruiterServiceImpl.getTaggedJobString(principal, falg), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/sent-profiles")
	public ResponseEntity<List<CandidateProfilesResponse>> sentProfilesByJobString(Principal principal, @RequestParam int jobStringId) {
         
		return new ResponseEntity<List<CandidateProfilesResponse>>(
				 this.recruiterServiceImpl.getSentProfilesByJob(principal, jobStringId), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/posted-job-profiles")
	public ResponseEntity<List<CandidateProfilesResponse>> getPostedJobProfiles(@RequestParam int jobStringId) {
         
		return new ResponseEntity<List<CandidateProfilesResponse>>(
				 this.recruiterServiceImpl.getPostedJobProfilesByJobStringId( jobStringId), HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/interview-status")
	public ResponseEntity<?> updatedInterviewStatus(@RequestBody InterviewScheduleStatusRequest schedule ) {
		
		 this.recruiterServiceImpl.updateInterviewStatus( schedule);
		 
		return new ResponseEntity<InterviewScheduleStatusRequest>( HttpStatus.OK);
	}
	
	@GetMapping("/profile-tracking")
	public ResponseEntity<Map<String,Long>> candidateProfileTracking(@RequestParam Integer jobId,Principal principal) {
		
		 	 
		return new ResponseEntity<Map<String,Long>>(this.recruiterServiceImpl.sentProfilesTracking(jobId, principal), HttpStatus.OK);
	}
	
	

}
