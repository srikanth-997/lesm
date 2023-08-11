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

import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;

@Service
public class PoEdateRemainderMail {

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	EmployeesAtClientsDetailsRepository atClientsDetailsRepository;
	@Scheduled(cron = "0 0 6 * * *")
	public void PoEdateRemainder() {
		List<MasterEmployeeDetails> masterEmployeeDetails = masterEmployeeDetailsRepository.findAll();
		if(!masterEmployeeDetails.isEmpty())
		{
		for (MasterEmployeeDetails employee : masterEmployeeDetails) {

			List<EmployeesAtClientsDetails> recordsOfEmployee = atClientsDetailsRepository
					.findsBymasterEmployeeDetails_Id(employee.getEmpId());

			if (!recordsOfEmployee.isEmpty()) {
				
				
				for (EmployeesAtClientsDetails record : recordsOfEmployee) {

					if (record.getPOEdate().isAfter(LocalDate.now()) || record.getPOEdate().equals(LocalDate.now()) ) {

						LocalDate schedule = record.getPOEdate();

//						if (schedule.minusDays(60).equals( LocalDate.now())) {
//							sendEmail(employee, record);
//						}
//
//						if (schedule.minusDays(30).equals( LocalDate.now())) {
//							sendEmail(employee, record);
//						}
//
//						if (schedule.minusDays(7).equals( LocalDate.now())) {
//							sendEmail(employee, record);
//						}
//
//						if (schedule.minusDays(1).equals( LocalDate.now())) {
//							sendEmail(employee, record);
//						}
						
						if (schedule.getMonthValue() == LocalDate.now().getMonthValue()
								&& schedule.getDayOfMonth()  == LocalDate.now().getDayOfMonth()) {
							
							sendEmail(employee, record);
						}
						
						
						
						
					}
				}
			}
			else
			{
				
				System.out.println("No Client records Found");
			}
		}
	}
	
	else
	{
		System.out.println("No Employees records Found");
		
	}
	}

	public void sendEmail(MasterEmployeeDetails employee, EmployeesAtClientsDetails record) {

		MasterEmployeeDetails supervisor = employee.getSupervisor();

		String consultantMessage = "Hi " + employee.getFirstName() + ", \nyour contract tenure is coming to end "
				+ record.getPOEdate() + ", pls do follow up with your managers \r\n" + "1. " + supervisor.getEmail()+"("+supervisor.getFirstName()+")\n"
				
				+ "2. " + record.getClientEmail()+"("+ record.getClientManagerName() + ")"
					     +"\n\n\nThank you\nTeam Lancesoft";

		sendMailTo(consultantMessage, employee.getEmail());

		String InternalManagerMessage = "Hi " + supervisor.getFirstName() + ",\n your consultant "
				+ employee.getFirstName() + " " + employee.getLancesoft() + " is coming on bench " + record.getPOEdate()
				+ ", please do follow up with " + record.getClientManagerName()
			     +"\n\n\nThank you\nTeam Lancesoft";

		sendMailTo(InternalManagerMessage, supervisor.getEmail());

		String ClientManagerMessage = "Hi " + record.getClientManagerName() + ",\nyour consultant "
				+ employee.getFirstName() + " " + employee.getLancesoft() + ", Lancesoft, Po is coming to end "
				+ record.getPOEdate() + ", Please extend the PO"
					     +"\n\n\nThank you\nTeam Lancesoft";

		sendMailTo(ClientManagerMessage, record.getClientEmail());

	}

	public void sendMailTo(String message, String to) {

		String subject = "PURCHASE ORDER END-DATE REMAINDER";

		//String from = "sudheer.beesamalla@gmail.com";
		String from="lancesoft.domestic@gmail.com";
		
		String password="wkpniiwoynctwxfx";

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
				//return new PasswordAuthentication(from, "hlhvbsjfkqspkyqf");
				return new PasswordAuthentication(from,password);
				
				
			}
		});

		// session.setDebug(true);

		// step2 compose text,multi-media(audio,image,attachment..etc)

		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			mimeMessage.setSubject(subject);
			mimeMessage.setText(message);

			Transport.send(mimeMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
