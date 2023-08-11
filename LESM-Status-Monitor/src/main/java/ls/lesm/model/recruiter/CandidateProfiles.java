package ls.lesm.model.recruiter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.MasterEmployeeDetails;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfiles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "prfl_id_gen")
	@GenericGenerator(name="prfl_id_gen", strategy="ls.lesm.model.recruiter.CustomeIdGenerator", parameters = {
			@Parameter(name=CustomeIdGenerator.INCREMENT_PARAM, value="1"),
			@Parameter(name=CustomeIdGenerator.VALUE_PREFIX_PARAMAETER, value="EMP_"),
			@Parameter(name=CustomeIdGenerator.NUMBER_FORMAT_PARAMETER, value="%03d")
	})
	private String candidateId;
	
	@Column(length=90)
	private String candidateName;
	
	@Column(length=80)
	private String emailId;
	
	private Long mobileNo;
	
	private Integer currentCTC;
	
	private Integer expectedCTC;
	
	@Column(length=20)
	private String relevantExp;
	
	@Column(length=20)
	private String totalExp;
	
	@Column(length=30)
	private String currentOrg;
	
	private String candiResume;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	private Status managerApproval;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime approveAt;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="job_str_id")
	private JobString jobString;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_rec_id")
	private MasterEmployeeDetails masterEmployeeDetails;
	

}
