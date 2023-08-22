package ls.lesm.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
//umer
public class AuthUserNotFoundExceptions extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String message;

}
