package ls.lesm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
	
      private Integer empId;
      private String employeeId;
      private String firstName;
      private String lastName;
      private String dob;
      private String location;
      private String email;
      private String gender;
      private String designation;
      private String employeeType;
      private String subDepartName;
      private String joiningDate;
      private String vertical;
      private Long phoneNo;
      private String status;
      private String department;
      private String technology1;
      private String technology2;
      private Integer aadharNumber;
      private String PANNumber;
}
