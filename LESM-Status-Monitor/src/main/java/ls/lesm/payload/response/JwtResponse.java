package ls.lesm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
//UMER
public class JwtResponse {

	String token;
	String role;
	String firstName;
	String lastName;
	String lancesoft;
	Integer empId;
}
