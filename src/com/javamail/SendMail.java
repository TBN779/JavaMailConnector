package com.javamail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * @author TungNguyen
 * */
public class SendMail  {
    Properties properties = null;
    private Session session = null;
    private Message message = null;
    String fromId;
    String toId;
    String returncheck = "";
    
    public SendMail(){}
    
    
	public void sendMail(final String fromAddress, final String passWord, String toAddress, String subject, String content) throws FileNotFoundException, IOException {
		
	    properties = new Properties();
	    properties.setProperty("mail.smtp.auth", "true");
	    properties.setProperty("mail.smtp.starttls.enable", "true");
	    properties.setProperty("mail.smtp.host", "smtp.gmail.com");
	    properties.setProperty("mail.smtp.port", "587");   
	    
	    session = Session.getInstance(properties, new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(fromAddress, passWord);
	        }
	    });
	     
	    
	    message = new MimeMessage(session);
	    
	    String msg = "<font color=#002b54>The request need to approve. <br><br> ";
        		
        msg += content;
	    try {
	        message.setFrom(new InternetAddress(fromAddress));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
	        message.setSubject(subject);
	        message.setContent(msg, "text/html");
	        Transport.send(message);
	    } catch (AddressException e) {
	        e.printStackTrace();
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    } 
	    
	}


}
