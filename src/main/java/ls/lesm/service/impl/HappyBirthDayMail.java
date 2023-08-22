package ls.lesm.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.MasterEmployeeDetailsRepository;

@Service
public class HappyBirthDayMail {
	
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
    @Scheduled(cron = "0 0 6 * * *")
	public void happyBirthDay() {

		List<MasterEmployeeDetails> masterEmployeeDetails = masterEmployeeDetailsRepository.findAll();
		for (MasterEmployeeDetails employeeDetails : masterEmployeeDetails) {
			if (employeeDetails.getDOB().equals(LocalDate.now())) {

				List<MasterEmployeeDetails> employeesWithsameSupervisor = masterEmployeeDetailsRepository
						.findBymasterEmployeeDetails_Id(employeeDetails.getSupervisor().getEmpId());

				for (MasterEmployeeDetails employee : employeesWithsameSupervisor) {
					sendEmail(employeeDetails, employee.getEmail());

				}

				sendEmail(employeeDetails, employeeDetails.getSupervisor().getEmail());

			}

		}

	}
    
    //

	public void sendEmail(MasterEmployeeDetails Employee, String to) {

		String subject = "Happy Birthday ";

		//String from = "sudheer.beesamalla@gmail.com";
        String from="lancesoft.domestic@gmail.com";
		
		//String password="wkpniiwoynctwxfx";
		// variable
		String host = "smtp.gmail.com"; // This is a server

		Properties properties = System.getProperties();
		System.out.println("properties" + properties);

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// step1 get the session Object

		Session session = Session.getDefaultInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "wkpniiwoynctwxfx");
			}
		});

		// session.setDebug(true);

		// step2 compose text,multi-media(audio,image,attachment..etc)
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			mimeMessage.setSubject(subject);

			mimeMessage.setContent("<h1 style=color:yellow>HAPPY BIRTHDAY</h1>\n" + "<h2 style=color:green>"
					+ Employee.getFirstName()+"  "+  Employee.getLastName()  + "</h2>\n"
					+ "<h3 style=color:blue>wish you a beautiful day and many blessings for the year ahead</h3>\n" + "",
					"text/html");

			Transport.send(mimeMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
