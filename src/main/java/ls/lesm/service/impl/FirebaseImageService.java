package ls.lesm.service.impl;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Optional;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import io.grpc.internal.IoUtils;
import lombok.Data;
import ls.lesm.model.EmployeePhoto;
import ls.lesm.repository.EmployeePhotoRepo;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.service.IImageService;

@Service
@WebListener
public class FirebaseImageService implements IImageService, ServletContextListener {

	/*
	 * @author UMER
	 * 
	 * @since 0.1
	 * 
	 * @see Firebase storage service
	 */

	@Autowired
	Properties properties;
	
	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	private EmployeePhotoRepo employeePhotoRepo;
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("ServletContextListener destroyed");
		
	}
	

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		try {

			ClassPathResource serviceAccount = new ClassPathResource("firebase.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
					.setStorageBucket(properties.getBucketName()).build();

			FirebaseApp.initializeApp(options);

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}
	@Override
	public Object download(int empId) throws FileNotFoundException, IOException {
		EmployeePhoto photo=this.employeePhotoRepo.findByMasterEmployeeDetails(this.masterEmployeeDetailsRepository.findById(empId).get()).get();
		String filePath=photo.getProfilePic();
		String fileName = filePath.substring(55);
		//System.out.print("-------------"+fileName);
		
		ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
		
		Credentials credentials = GoogleCredentials.fromStream(serviceAccount.getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        
        
        Blob blob = storage.get(properties.getBucketName(),fileName);
        ReadChannel reader = blob.reader();
        InputStream inputStream = Channels.newInputStream(reader);

        byte[]  content =IoUtils.toByteArray(inputStream);

        final ByteArrayResource byteArrayResource = new ByteArrayResource(content);

        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath + "\"")
                .body(byteArrayResource);
	}
	
	

//	@EventListener
//	public void init(ApplicationReadyEvent event) {
//
//		// initialize Firebase
//
//	}

	@Override
	public String getImageUrl(String name) {
		return String.format(properties.imageUrl, name);
	}

	@Override
	public String save(MultipartFile file) throws IOException {

		Bucket bucket = StorageClient.getInstance().bucket();

		String name = generateFileName(file.getOriginalFilename());

		bucket.create(name, file.getBytes(), file.getContentType());

		return name;
	}

	@Override
	public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

		byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

		Bucket bucket = StorageClient.getInstance().bucket();

		String name = generateFileName(originalFileName);

		bucket.create(name, bytes);

		return name;
	}

	@Override
	public void delete(int empId) throws IOException {
	Optional<EmployeePhoto> photo=this.employeePhotoRepo.findByMasterEmployeeDetails(this.masterEmployeeDetailsRepository.findById(empId).get());
	if(photo.isPresent()) {
     String fileName=photo.get().getProfilePic().substring(55);
	
		Bucket bucket = StorageClient.getInstance().bucket();
		
		if (StringUtils.isEmpty(fileName)) {
			throw new IOException("invalid file name");
		}

		Blob blob = bucket.get(fileName);

		if (blob == null) {
			throw new IOException("file not found");
		}
		

		blob.delete();
	}
		
	}

	@Data
	@Configuration
	@ConfigurationProperties(prefix = "firebase")
	public class Properties {

		private String bucketName;

		private String imageUrl;
	}

}