package ls.lesm.securityconfig;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import ls.lesm.exception.TokenIsNotValidException;
import ls.lesm.service.impl.UserDetailsServiceImpl;

@Component // this class will work as a filter, means this class work before request
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");// (1)token will come here from client,
		
		Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements())
        {
             String header = (String) enumeration.nextElement();
             System.err.println(header + ": " + request.getHeader(header) + "");
        }
		
		// w'll store in requestTokenHeader
		System.out.println(requestTokenHeader);
		String username = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			// yes

			jwtToken = requestTokenHeader.substring(7);// (2) w'll remove tokn from header by removing bearer

			try {
				username = this.jwtUtil.extractUsername(jwtToken);// (3) extracting username
			} catch (ExpiredJwtException e) {
				e.printStackTrace();
				System.out.println("jwt token has expired");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("error");
			}

		} else {
			System.out.println("Invalid token , not start with bearer string");
		}

		// (4) here we have uname and token w'll validate it hare
		// validated
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (this.jwtUtil.validateToken(jwtToken, userDetails)) {
				// token is valid

				UsernamePasswordAuthenticationToken usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));// (5)
																														// here
																														// w'll
																														// set
																														// details
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
			}
		} else {
			System.out.println("Token is not valid");
			//throw new TokenIsNotValidException("Token not valid");

		}

		filterChain.doFilter(request, response);
	}

}
