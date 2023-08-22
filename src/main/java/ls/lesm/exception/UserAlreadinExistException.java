package ls.lesm.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component

public class UserAlreadinExistException extends RuntimeException{

private static final long serialVersionUID = 1L;
	
	public UserAlreadinExistException(String message)
	{
		super(message);
	}
	////

}
