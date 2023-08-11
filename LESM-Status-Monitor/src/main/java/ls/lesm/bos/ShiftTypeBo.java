package ls.lesm.bos;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftTypeBo {

	private Integer id;

	private LocalDateTime createdAt;

	private LocalDateTime editedAt;

	private Integer createdLoginId;

	private Integer editedLoginId;

	private String shiftCode;

	private String shftDesc;

	private LocalTime startTime;

	private LocalTime endTime;

}
