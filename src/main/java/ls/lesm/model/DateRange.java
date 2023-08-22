package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@NoArgsConstructor
@ToString
public class DateRange {
	

	@Id
	private int drId;

	//@JsonIgnore
	private LocalDate fromDate;

	//@JsonIgnore
	private LocalDate toDate;

}