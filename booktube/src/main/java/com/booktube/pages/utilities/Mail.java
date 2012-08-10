package com.booktube.pages.utilities;

import org.apache.log4j.Logger;


import com.jscape.inet.email.EmailMessage;
import com.jscape.inet.smtp.SmtpException;
import com.jscape.inet.smtpssl.SmtpSsl;


public class Mail {
	static public void send(String dest, String subject, String body) throws Exception{
		SmtpSsl smtp = null;
		EmailMessage mail = null;

		smtp = new SmtpSsl("smtp.gmail.com", 465);

		mail = new EmailMessage();
		mail.setTo(dest);
		mail.setFrom("proyectoBooktube@gmail.com");
		mail.setSubject(subject);
		mail.setContentType("text/html");
		mail.setBody(body);
		
		
		smtp.connect();
		smtp.login("proyectoBooktube@gmail.com", "hugolorenzorobert");
		try {
			smtp.send(mail);
			Logger.getLogger("helper.Mail").info("se mando un mail a "+dest);
		} catch (SmtpException e) {
			Logger.getLogger("helper.Mail").warn("no se pudo mandar un mail a "+dest);
			throw new Exception("fallo al mandar mail");
		}
		
		smtp.disconnect();
	}
}