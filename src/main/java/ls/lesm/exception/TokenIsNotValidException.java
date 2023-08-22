package ls.lesm.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor

public class TokenIsNotValidException extends RuntimeException {



	private static final long serialVersionUID = 1L;
	
	public TokenIsNotValidException(String message)
	{
		super(message);
	}

}
