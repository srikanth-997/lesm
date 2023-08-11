package ls.lesm.payload.request;

import java.util.List;

import lombok.Data;
import ls.lesm.model.Address;
import ls.lesm.model.Departments;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.SubDepartments;
@Data
public class EmployeeDetailsUpdateRequest {
	
	private MasterEmployeeDetails masterEmployeeDetails;
	private Address address;
	private double salary;
	private int departments;
	private int designations;
	private int employeeType;
	private int subDepartments;
	private int supervisor;
	private int technology1;
	private String technology2;
	private Integer supervisorDesig;
	
	
	
	

}
