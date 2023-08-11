package ls.lesm.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmployeePhoto {

	@Id
	@GeneratedValue(generator = "doc_gen", strategy = GenerationType.AUTO)
	private Integer docId;
	private String profilePic;

	@JsonIgnore
	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

}
