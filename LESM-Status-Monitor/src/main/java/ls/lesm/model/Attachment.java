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
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attachment_Id;
	private String fileName;
	private String fileType;
	@Lob
	private byte[] Content;

	private LocalDate createdAt;// timeStamp;

	@Column(length = 20)
	private String createdBy;// principal

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "emp_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

	public Attachment(String fileName, String fileType, byte[] content) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		Content = content;
	}

	public Attachment(String fileName, String fileType, byte[] content, MasterEmployeeDetails masterEmployeeDetails) {
		super();
		this.attachment_Id = attachment_Id;
		this.fileName = fileName;
		this.fileType = fileType;
		Content = content;
		this.masterEmployeeDetails = masterEmployeeDetails;
	}

	public Attachment(String fileName, String fileType, byte[] content, LocalDate createdAt, String createdBy) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		Content = content;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		
	}

}
