package  ls.lesm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
	
	@Id
	private Integer roleId;
	private String roleName;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, mappedBy="role")
	private Set<UserRole> userRole=new HashSet<>();

}
