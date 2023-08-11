package ls.lesm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Technology {
	
	@Id
	@GeneratedValue(generator = "id_gen",strategy = GenerationType.AUTO)
	private Integer id;
	
	private String technology;
	
	
}
