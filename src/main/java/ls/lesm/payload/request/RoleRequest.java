package ls.lesm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//UMER
public class RoleRequest {
	
	private Integer roleId;
	private String roleName;

}
