package ls.lesm.exception;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component

public class EmptyInputException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public EmptyInputException(String message)
	{
		super(message);
	}

}
