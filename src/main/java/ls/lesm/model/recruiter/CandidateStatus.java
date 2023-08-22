package ls.lesm.model.recruiter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateStatus {
	
	@Id
	@GeneratedValue(generator = "st_id_gen",strategy = GenerationType.AUTO)
	private Integer statusId;
	
	private LocalDateTime L1ScheduleAt;
	
	private LocalDateTime L1PostponedAt;
	
	private Status L1Status;
	
	private LocalDateTime L2ScheduleAt;
	
	private LocalDateTime L2PostponedAt;
	
	private Status L2Status;
	
	private boolean releasedOffer;//yes/no
	
	private LocalDate releasedOfferAt;
	
	private boolean joined;//yes/no
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="consultan_id")
	private Consultant consultant;
	
	
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_rec_id")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="job_string_id")
	private JobString jobString;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="candidate_id")
	private CandidateProfiles candidateProfiles;
	
	
	
	
	

}
