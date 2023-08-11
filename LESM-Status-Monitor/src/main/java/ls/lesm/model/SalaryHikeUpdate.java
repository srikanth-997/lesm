package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class SalaryHikeUpdate {

	@Id
	private int id;

	private String EmpId;
	private double salary;
	private LocalDate updatedAt;

}
