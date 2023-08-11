package ls.lesm.repository.recruiter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ls.lesm.model.Clients;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.recruiter.JobString;

public interface JobStringRepo extends JpaRepository<JobString, Integer > ,JpaSpecificationExecutor<JobString> {

	List<JobString> findAllByTicketStatus(boolean b);

	List<JobString> findAllByMasterEmployeeDetailsAndTicketStatus(MasterEmployeeDetails loggedInEmp, boolean b);

	List<JobString> findByStringCreatedByAndTicketStatus(String string, boolean flag);

	List<JobString> findByStringCreatedByAndTicketStatusOrMasterEmployeeDetails(String string, boolean flag, MasterEmployeeDetails employee);

	List<JobString> findByStringCreatedByAndTicketStatusAndMasterEmployeeDetails(String lancesoft, boolean flag,
			MasterEmployeeDetails employee);

	List<JobString> findByStringCreatedByOrMasterEmployeeDetailsAndTicketStatus(String lancesoft,
			MasterEmployeeDetails employee, boolean flag);

	List<JobString> findByMasterEmployeeDetailsAndTicketStatus(MasterEmployeeDetails employee, boolean flag);

	List<JobString> findByStringCreatedByAndTicketStatusOrCreatedAtOrHiringTypeOrClientId(String lancesoft,
			boolean flag, LocalDate date, EmployeeType employeeType, Clients clients);

	List<JobString> findByStringCreatedByAndTicketStatusAndCreatedAtAndHiringTypeAndClientId(String lancesoft,
			boolean flag, LocalDate date, EmployeeType employeeType, Clients clients);

	List<JobString> findAll(Specification<JobString> specification);

	//void findAllByMasterEmployeeDetails();

}
