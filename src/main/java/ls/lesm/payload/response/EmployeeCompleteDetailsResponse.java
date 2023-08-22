package ls.lesm.payload.response;

import java.util.List;

import lombok.Data;
import ls.lesm.model.AtClientAllowances;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
@Data
public class EmployeeCompleteDetailsResponse {
	
	private EmployeeDetailResponse detailsResponse;
	private String salary;
	private String allClientsEarning;
	private String lastStatus;
	private String releasedDate;
	private String exitType;
	private String profile;
	private List<InternalExpensesResponse> internalExpenses;
	private List<EmployeeAtClientResponse> employeeAtClientsDetails;
	private List<ls.lesm.model.Address> addres;
	
	private List<AtClientAllowances> atClientAllowances;

}
