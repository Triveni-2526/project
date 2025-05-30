package com.user.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {

	private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
//	public EmailUtils(JavaMailSender mailSender) {
//		super();
//		this.mailSender = mailSender;
//	}

	public boolean sendEmail(String to, String subject, String body){
		boolean isMailSent= false;
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body,true);
			mailSender.send(message);
			isMailSent=true;
		} catch (Exception e) {
			logger.error("exception occured",e);
		}
		return isMailSent;
	}
}
