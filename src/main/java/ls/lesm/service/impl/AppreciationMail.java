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
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.repository.MasterEmployeeDetailsRepository;

@Service
public class AppreciationMail {

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	 @Scheduled(cron = "0 0 6 * * *")

	// @Scheduled(cron = "* * * * * *")

	public void appriciation() {
		List<MasterEmployeeDetails> masterEmployeeDetails = masterEmployeeDetailsRepository.findAll();

		for (MasterEmployeeDetails m : masterEmployeeDetails) {

			if (!m.getStatus().equals(EmployeeStatus.EXIT)) {

				LocalDate joininDate = m.getJoiningDate();

//				if (joininDate.getMonthValue() == LocalDate.now().getMonthValue()
//						&& joininDate.getDayOfMonth() - 1 == LocalDate.now().getDayOfMonth()) {

				if (joininDate.getMonthValue() == LocalDate.now().getMonthValue()
						&& joininDate.getDayOfMonth()  == LocalDate.now().getDayOfMonth()) {

					sendApprisalMailToManager(m);

				}

				if (joininDate.getMonthValue() == LocalDate.now().getMonthValue()
						&& joininDate.getDayOfMonth() == LocalDate.now().getDayOfMonth()) {

					sendApprisalMailToConsultant(m);

				}

			}

		}
	}

	public void sendApprisalMailToManager(MasterEmployeeDetails Employee) {
		MasterEmployeeDetails supervisor = Employee.getSupervisor();

		// manager
		String Message = "Hi " + supervisor.getFirstName() + ", your consultant " + Employee.getFirstName()
				+ "  Appraisal is coming on " + Employee.getJoiningDate() + ", please initiate"
				+ "\n\n\nThank you\nTeam Lancesoft";

		String to = supervisor.getEmail();

		sendApprisalMail(Message, to);

	}

	public void sendApprisalMailToConsultant(MasterEmployeeDetails Employee) {

		String Message = "Hi " + Employee.getFirstName()
				+ ", your Appraisal started, please connect your Reporting manager," + " Lancesoft. on "
				+ Employee.getJoiningDate() + "\n\n\nThank you\nTeam Lancesoft";

		String to = Employee.getEmail();
		sendApprisalMail(Message, to);

	}

	public void sendApprisalMail(String message, String to) {

		String Subject = " Appraisal for Anual ";

		String from = "sudheer.beesamalla@gmail.com";

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
				return new PasswordAuthentication(from, "hlhvbsjfkqspkyqf");
			}
		});

		// session.setDebug(true);

		// step2 compose text,multi-media(audio,image,attachment..etc)
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			mimeMessage.setSubject(Subject);
			mimeMessage.setText(message);

			Transport.send(mimeMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
