package ls.lesm.payload.request;

import java.time.LocalDate;

import lombok.Data;
import ls.lesm.model.enums.EmployeeStatus;

@Data
public class EmployeeDetailsSalAndAddressUpdateReq {
	
	private Integer empId;
	private String firstName;
	private String lastName;
	private String email;
	private Long phoneNo;
	private String location;
	private String employeeType;
	private EmployeeStatus status;
	private String department;
	private String subDepartment;
	private String vertical;
	private String lancesoftId;
	private String designation;
	private String supervisor;
	private String country;
	private String state;
	private String city;
	private String street;
	private Long pincode;
	private String addressType;
	private boolean isInternal;
	private Double Salary;
	private LocalDate joiningDate;
	private LocalDate dateOfBirth;
	private String gender;
	
}
