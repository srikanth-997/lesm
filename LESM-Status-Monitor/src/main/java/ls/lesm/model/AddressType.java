package  ls.lesm.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressType {
	@Id
	@GeneratedValue(generator = "addtype_gen",strategy = GenerationType.AUTO)
	private Integer addTypeId;
	
	@Column(length=20)
	private String addType;
	
}
