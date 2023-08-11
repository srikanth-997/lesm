package ls.lesm.model.timesheet;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Mode  implements Serializable{

	@Id
	@GeneratedValue(generator = "int_tgden", strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;
	@Column(name = "EditedAt")
	private LocalDateTime editedAt;
	@Column(name = "CreatedLoginId")
	private Integer createdLoginId;
	@Column(name = "EditedLoginId")
	private Integer editedLoginId;

	@Column(name = "Mode")
	private String mode;

	@Column(name = "Description")
	private String desc;
}
