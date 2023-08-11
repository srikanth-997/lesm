package ls.lesm.service.impl;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ls.lesm.exception.FileformatException;
import ls.lesm.model.Attachment;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.AttachementRepo;
import ls.lesm.repository.MasterEmployeeDetailsRepository;

@Service
public class AttachmentService {
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	AttachementRepo attachementRepo;

	public void saveAttachment(MultipartFile multipartFile, int id, String lancesoft) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		if (fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx")
				|| fileName.endsWith(".txt")) {

			Attachment attachment = new Attachment(fileName, multipartFile.getContentType(), multipartFile.getBytes(),
					LocalDate.now(), lancesoft);

			MasterEmployeeDetails masterEmployeeDetails = masterEmployeeDetailsRepository.findById(id).get();

			attachment.setMasterEmployeeDetails(masterEmployeeDetails);
			attachementRepo.save(attachment);

		} else {
			throw new FileformatException("filename must be ends with pdf or docs");
		}

	}

//	@Override
//	public Attachment getAttachment(int fileId) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	public Attachment getAttachment(int Id) throws Exception {
//		
//		return attachementRepo.findById(Id).get();
//                
//              
//	}
//	
//	

}
