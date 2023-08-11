package ls.lesm.payload.response;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.Salary;
@Data
//UMER
public class EmpCorrespondingDetailsResponse {
	
	private InternalExpenses internalExpenses;
	private Salary salary;
	private EmployeeDetailsResponse employeeDetailsResponse;
	

}
