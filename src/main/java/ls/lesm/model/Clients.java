package  ls.lesm.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Clients {
	
	@Id
	@GeneratedValue(generator = "Clients_gen",strategy = GenerationType.AUTO)
	private Integer clientsId;
	
	@Column(length=30)
	private String clientsNames;
	
	@JsonIgnore
	private Date CreatedAt;// timeStamp
	
	@JsonIgnore
	@Column(length=30)
	private String createdBy;// principal

}
