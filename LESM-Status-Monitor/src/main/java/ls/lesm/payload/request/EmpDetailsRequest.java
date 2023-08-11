package ls.lesm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//UMER
public class EmpDetailsRequest {
	
	private Integer superviserId;
	private Integer EmpTypeId;
	private Integer verticalId;
	private Integer desgId;
	private Integer subDepartId;
	private Integer departId;

}
