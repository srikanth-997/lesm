package ls.lesm.bos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayCalenderBo {
	

	private Integer id;


	private LocalDateTime createdAt;

	private LocalDateTime editedAt;

	private Integer createdLoginId;

	private Integer editedLoginId;
	
	private LocalDate holidayDate;
	
	private String holidayName;
	
	private String desc;
	
	private Integer year;

}
