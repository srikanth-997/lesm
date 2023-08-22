package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubDepartments {

	@Id
	@GeneratedValue(generator = "sub_depart_gen", strategy = GenerationType.AUTO)
	private Integer subDepartId;

	@Column(length = 30)
	private String subDepartmentNames;

	private LocalDate createdAt;

	@Column(length = 30)
	private String createdBy;// principal

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "depart_fk")
	private Departments departments;

}
