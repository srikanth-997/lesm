package ls.lesm.exception;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component

public class UserNameNotFoundException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	
	public UserNameNotFoundException(String message){
		super(message); 
	}
		

}