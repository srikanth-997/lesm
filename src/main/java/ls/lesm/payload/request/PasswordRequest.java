package ls.lesm.payload.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//UMER
public class PasswordRequest {

	private String oldPassword;
	private String newPassword;
}
