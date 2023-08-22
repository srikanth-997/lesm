package ls.lesm.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import ls.lesm.model.recruiter.CandidateProfiles;
import ls.lesm.model.recruiter.JobString;
import ls.lesm.model.recruiter.RecruiterProfitOrLoss;
import ls.lesm.payload.request.ApproveProfilesRequest;
import ls.lesm.payload.request.InterviewScheduleStatusRequest;
import ls.lesm.payload.response.CandidateProfilesResponse;
import ls.lesm.payload.response.EmployeesDropDownResponse;
import ls.lesm.payload.response.JobStringResponse;

public interface RecruiterService {
	
	String postJobString(JobString jobString, Principal principal, Set<Integer> empIds,MultipartFile file, Integer clientId, Integer hiringTypeId);
	
	void closeTicketJob();
	
	List<JobString> getOpenedJobStringByLoggedInEmp(Principal principal);
	
	String sendProfiles(CandidateProfiles profiles, Integer jobStringId, Principal principal,MultipartFile resume);
	
	List<EmployeesDropDownResponse> recruitersDropDown(Principal principal);
	
	void managerProfileApproval(List<ApproveProfilesRequest> req);

	InterviewScheduleStatusRequest scheduleInterview(String condidateProfileId, Principal principal);
	
	List<RecruiterProfitOrLoss> getRecruiterProfitOrLoss(Principal principal);
    
	List<JobStringResponse> getPostedJobStringByLoggedIn(Principal principal, boolean flag);
	
	List<JobStringResponse> getPostedJobStringByLoggedInAndFillters(Principal principal, boolean flag, LocalDate date, Integer hiringType, Integer clientId);
	
	List<JobStringResponse> getTaggedJobString(Principal principal, boolean flag);

	List<CandidateProfilesResponse> getSentProfilesByJob(Principal principal, int jobStringId);
	
	List<CandidateProfilesResponse> getPostedJobProfilesByJobStringId(int jobStringId);
	
	void updateInterviewStatus(InterviewScheduleStatusRequest schedule);
	
	 Map<String,Long> sentProfilesTracking(Integer jobId,Principal principal);
	
	
}
