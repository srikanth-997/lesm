package ls.lesm.service;

import java.security.Principal;

import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.payload.request.EmployeeAtClientRequest;

public interface ClientService {
	//UMER
	EmployeeAtClientRequest addClientDetailsToEmployee(EmployeeAtClientRequest clientDetails, Principal principal);
	
	
	void editClientDetailsForEmployee(Integer clientDetailId,EmployeeAtClientRequest clientDetails, Principal principal );
	
	EmployeeAtClientRequest editClientDetailRequest(Integer clientDetailId);
}
