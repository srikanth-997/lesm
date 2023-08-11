package ls.lesm.exception;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor

public class SupervisorAlreadyExistException extends RuntimeException {



	private static final long serialVersionUID = 1L;
	
	public SupervisorAlreadyExistException(String message)
	{
		super(message);
	}

}
