package ls.lesm.bos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InternalProjectBo {

	private Integer id;

	private LocalDateTime createdAt;

	private LocalDateTime editedAt;

	private Integer createdLoginId;

	private Integer editedLoginId;

	private String projectTitle;

	private String projectDesc;

	private Integer teamSize;

}
