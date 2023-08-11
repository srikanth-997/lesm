package ls.lesm.securityconfig;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import ls.lesm.exception.RestError;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	 @Autowired
	 @Qualifier("handlerExceptionResolver")
	 private HandlerExceptionResolver resolver;
	 
	  @Override
	    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

	        RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(), "Incorrect login credentials i.e. username or password!");
	        
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        OutputStream responseStream = response.getOutputStream();
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.writeValue(responseStream, re);
	        responseStream.flush();

	    }
}