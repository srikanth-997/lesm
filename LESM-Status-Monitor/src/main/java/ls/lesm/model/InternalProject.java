package ls.lesm.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InternalProject {
	

	@Id
	@GeneratedValue(generator = "int_pgen",strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name="CreatedAt")
	private LocalDateTime createdAt;
	@Column(name="EditedAt")
	private LocalDateTime editedAt;
	@Column(name="CreatedLoginId")
	private Integer createdLoginId;
	@Column(name="EditedLoginId")
	private Integer editedLoginId;
	
	@Column(name="ProjectTile")
	private String projectTitle;
	
	@Column(name="ProjectDesc")
	private String projectDesc;
	
	@Column(name="TeamSize")
	private Integer teamSize;
	
	
	
	
	

}
