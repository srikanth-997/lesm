package ls.lesm.exception;

import lombok.Data;

//UMER
@Data
public class UserNotRegisterd extends RuntimeException{

	public UserNotRegisterd(String string) {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String message; {
	}
}
