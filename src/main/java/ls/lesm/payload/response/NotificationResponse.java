package ls.lesm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter 
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
	
	private Integer notificationId;
	
	private boolean flag;
	
	private String message;
	
	private String createdAt;

}
