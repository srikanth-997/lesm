package ls.lesm.model;



import lombok.Data;

@Data
public class ResponseData {
	private String fileName;
	private String downloadUrl;
	private String fileType;
	private long fileSize;
	public ResponseData(String fileName, String downloadUrl, String fileType, long fileSize) {
		super();
		this.fileName = fileName;
		this.downloadUrl = downloadUrl;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}
	public ResponseData(String fileName, String fileType, long fileSize) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}
	
	
	
	
	
	
	
}

