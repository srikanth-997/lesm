package ls.lesm.securityconfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Value("{app.secret}")
    private String SECRET;// = "lhs_secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {//this method take userDetails service object in Map and create token by calling the create methtod 
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }/*
    this createToekn() method call jwt api and it uses a builder pattern and it setting a claims that we have passed in generateToken() 
    ans its set the subject means the person who is authenticated(taking usernaem in String subject param) and it set the curent system date & timne 
    ans finnaly signing in with SignatureAlgorithm.HS256 this algo and passing it in Secrete key taken it as a global var ans the compact will end this 
    */

    public Boolean validateToken(String token, UserDetails userDetails) {
    	   final String username = extractUsername(token);
           return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
       }/*
       this checks the username is match with the userDetails username and it also chcks is it expired ans it return boolean 
       */
   }
   