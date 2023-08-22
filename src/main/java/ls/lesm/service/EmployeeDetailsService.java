package ls.lesm.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import ls.lesm.model.Address;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.enums.ResponsibilitiesTypes;
import ls.lesm.payload.request.ClientEmpUpdateReq;
import ls.lesm.payload.request.EmployeeDetailsRequest;
import ls.lesm.payload.request.EmployeeDetailsUpdateRequest;
import ls.lesm.payload.response.AllEmpCardDetails;
import ls.lesm.payload.response.ConsultantDropDownRes;
import ls.lesm.payload.response.EmpCorrespondingDetailsResponse;
import ls.lesm.payload.response.EmployeeCompleteDetailsResponse;
import ls.lesm.payload.response.ReportToDropDownRes;

public interface EmployeeDetailsService {
	
	//UMER
	EmployeeDetailsRequest insetEmpDetails(EmployeeDetailsRequest empReq, Principal principal );

	
	//UMER
	Page<AllEmpCardDetails> getAllEmpCardDetails(PageRequest pageRequest);
	//UMER
	Page<AllEmpCardDetails> getSortedEmpCardDetailsByDesg(Integer desgId,PageRequest pageRequest);
	//UMER
	EmpCorrespondingDetailsResponse getEmpCorresDetails(EmpCorrespondingDetailsResponse corssDetailsint,int id);
	//UMER
	//EmployeeDetailsRequest updateEmployee(EmployeeDetailsRequest empReq);
	//UMER
	EmployeeDetailsUpdateRequest updateEmployee(EmployeeDetailsUpdateRequest empReq, int id);
	
	//UMER
	ClientEmpUpdateReq updateEmpClientDetails(ClientEmpUpdateReq req, int clientId);	
	//UMER
	EmployeeCompleteDetailsResponse empDetails(EmployeeCompleteDetailsResponse response,Integer empId);
	//UMER
	ClientEmpUpdateReq getClientDetailForUpdate(ClientEmpUpdateReq req,int clientId);
	//UMER
	void assignSencondSupervisor(int empId, int secSupervisorId, boolean flag);
	
	void assignResponsibilitiesToAnEmployee(Integer empId, List<ResponsibilitiesTypes> responsibilities);
	
	
	
	
	
	

	
	
}
