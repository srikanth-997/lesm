package ls.lesm.restcontroller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ls.lesm.exception.UserNameNotFoundException;
import ls.lesm.model.Attachment;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ResponseData;
import ls.lesm.repository.AttachementRepo;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.impl.AttachmentService;

@RestController

@CrossOrigin(value = { "*" }, exposedHeaders = { "Content-Disposition" })
public class ResumeController {

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	AttachementRepo attachmentRepo;
	@Autowired
	UserRepository userRepo;

	@PostMapping("/Resumeupload")
	public ResponseEntity<?> uploadingFile(@RequestParam MultipartFile file, @RequestParam String id,
			Principal principal) throws IOException {
		String lancesoft = this.userRepo.findByUsername(principal.getName()).getUsername();
	
		Double fileSize=(double) file.getSize() / (1024 * 1024);//in MB
		if(fileSize>5) {
			throw new UserNameNotFoundException("File size should not be more than 5 MB");
		}
		MasterEmployeeDetails employeeDetails = masterEmployeeDetailsRepository.findByLancesoft(id);
		if (employeeDetails == null) {
			throw new UserNameNotFoundException("Employee with that username not found");
		}

		Optional<Attachment> a = attachmentRepo.findBymasterEmployeeDetails_Id(employeeDetails.getEmpId());
		if (a.isPresent()) {
			throw new UserNameNotFoundException("Resume for this employee already there please update that");
		}

		attachmentService.saveAttachment(file, employeeDetails.getEmpId(), lancesoft);

		return new ResponseEntity("resume upload successfully", HttpStatus.OK);

	}

//	@GetMapping("/Resumedownload/{attachment_Id}")
//	public ResponseEntity<Resource> downloadFile(@PathVariable String attachment_Id) throws Exception {
//		Attachment attachment = null;
//		int i = Integer.parseInt(attachment_Id);
//		attachment = attachmentService.getAttachment(i);
//
//		return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getFileType()))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
//				.body(new ByteArrayResource(attachment.getContent()));
//	}
//	

	@GetMapping("/Resumedownload")
	public ResponseEntity<Resource> downloadFile(@RequestParam String lancesoft) throws Exception {

		MasterEmployeeDetails employeeDetails = masterEmployeeDetailsRepository.findByLancesoft(lancesoft);

		Optional<Attachment> attachment = attachmentRepo.findBymasterEmployeeDetails_Id(employeeDetails.getEmpId());

		if (attachment.isPresent()) {
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.get().getFileType()))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + attachment.get().getFileName() + "\"")
					.body(new ByteArrayResource(attachment.get().getContent()));

		} else {
			return new ResponseEntity("Resume does not exist", HttpStatus.NO_CONTENT);
		}

	}

	@PutMapping("/ResumeUpdate")
	public ResponseEntity<?> updatingFile(@RequestParam MultipartFile file, @RequestParam String id,
			Principal principal) throws Exception {

		String lancesoft = this.userRepo.findByUsername(principal.getName()).getUsername();

		MasterEmployeeDetails employeeDetails = masterEmployeeDetailsRepository.findByLancesoft(id);

		Optional<Attachment> a = attachmentRepo.findBymasterEmployeeDetails_Id(employeeDetails.getEmpId());

		if (a.isPresent()) {
			a.get().setFileName(file.getOriginalFilename());
			a.get().setContent(file.getBytes());
			a.get().setFileType(file.getContentType());
			a.get().setCreatedAt(LocalDate.now());
			a.get().setCreatedBy(lancesoft);
			attachmentRepo.save(a.get());

			return new ResponseEntity("resume updated successfully", HttpStatus.OK);

		}

		return new ResponseEntity("Resume does not exist", HttpStatus.NO_CONTENT);

	}

}
