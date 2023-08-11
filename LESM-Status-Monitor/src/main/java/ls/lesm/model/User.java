package  ls.lesm.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name="Users_Auth")
@JsonPropertyOrder({"id","username","firstName","lastName","email","phoneNo","DOB","password","gender","status"})
public class User implements UserDetails{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "auth_user_gen",strategy = GenerationType.AUTO)
	private Integer userId;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;
	@NotNull(message="lancesoft id is not null")
	@NotBlank(message="lancesoft id is not be blank")
	private String username;
	
	private String password;

	private String email;

	private Long phoneNo;

	private Boolean status=true;
	
	private String roleName;


	//user can have many 
	@OneToMany(fetch=FetchType.EAGER,mappedBy="user")
	@JsonIgnore
	private Set<UserRole> userRole=new HashSet<>();
	

	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<Authority> set = new HashSet<>();
		this.userRole.forEach(userRole -> {
			set.add(new Authority(userRole.getRole().getRoleName()));
		});
		return set;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Object isAccountNonExpired(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

}
