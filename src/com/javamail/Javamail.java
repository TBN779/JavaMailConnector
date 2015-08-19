package com.javamail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import teamworks.TWList;
import teamworks.TWObjectFactory;

/*
 * @author TungNguyen
 * */
public class Javamail  {
    Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;
    private String returnChecker="";  
    private Message message = null;
    String fromId;
    String toId;
    String returncheck = "";
    
    public Javamail(){}
    
	public Object processMessageBody(Message message) {
		Object o = null;
		try {
			Object content = message.getContent();
			// check for string
			// then check for multipart
			if (content instanceof String) {
				//System.out.println(content);
			} 
			else if (content instanceof Multipart) {
				Multipart multiPart = (Multipart) content;
				o = procesMultiPart(multiPart);
			} 
			else if (content instanceof InputStream) {
				@SuppressWarnings("resource")
				InputStream inStream = (InputStream) content;
				int ch;
				while ((ch = inStream.read()) != -1) {
					
					System.out.write(ch);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return o;
	}

	public Object procesMultiPart(Multipart content) {
		Object o = null;
		try {
			int multiPartCount = content.getCount();
			for (int i = 0; i < multiPartCount;) {
				BodyPart bodyPart = content.getBodyPart(i);
				
				o = bodyPart.getContent();
				if (o instanceof String) {
					//System.out.println(o);
				}
				//not string
				else if (o instanceof Multipart) {
					procesMultiPart((Multipart) o);
				}
				//just get content
				break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return o;
	}
    	
    public String returnChecker(final String userName, final String passWord, String keyWord) {
    	
        properties = new Properties();
        properties.setProperty("mail.host", "imap.gmail.com");
        properties.setProperty("mail.port", "995");
        properties.setProperty("mail.transport.protocol", "imaps");
        
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, passWord);
            }
        });
        
        try {
        	
            store = session.getStore("imaps");
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message messages[] = inbox.search(new FlagTerm( new Flags(Flag.SEEN), false));;
            
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];  
                //System.out.println(message.getSubject());
                
                if(message.getSubject().toString().contains(keyWord)){
                	if(processMessageBody(message).toString().contains("Approved")){
                	returnChecker = "approved";
                	}
                	if(processMessageBody(message).toString().contains("Rejected")){
                    	returnChecker = "rejected";
                    	}
                }
            }
            
            inbox.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return returnChecker;
        
    } 

    public TWList retrieveMail(final String userName, final String passWord, final String id) throws Exception {
		
		TWList twList = TWObjectFactory.createList();
		
		String temp;
		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH-mm-SS");
		
		properties = new Properties();
		
		properties.setProperty("mail.host", "imap.gmail.com");
		
		properties.setProperty("mail.port", "995");
		
		properties.setProperty("mail.transport.protocol", "imaps");
		
		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(userName, passWord);
			}
			
		});
		
		try {
			store = session.getStore("imaps");
			store.connect();
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			
			Message messages[] = inbox.search(new FlagTerm( new Flags(Flag.SEEN), false));;
					
			for (int i = 0; i < messages.length; i++) {	
				
				Message message = messages[i];
				
				if(message.getSubject().toString().contains(id)){		
					
					Address[] from = message.getFrom();
					
					processMessageBody(message);	
					
					temp = DATE_FORMAT.format(message.getSentDate());	
					
					twList.addArrayData(temp.toString() + "," + from[0].toString() + "," + message.getSubject().toString() + "," + processMessageBody(message).toString());
                }
				
			}
			
			inbox.close(true);
			store.close();
			
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}			
		
		return twList;	
	}
    
    
    public TWList retrieveAllMails(final String userName, final String passWord) throws Exception {
		
		TWList twList = TWObjectFactory.createList();
		String temp;
		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy:HH-mm-SS");
		
		properties = new Properties();
		properties.setProperty("mail.host", "imap.gmail.com");
		properties.setProperty("mail.port", "995");
		properties.setProperty("mail.transport.protocol", "imaps");
		
		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, passWord);
			}
		});
		
		try {
			store = session.getStore("imaps");
			store.connect();
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			
			Message messages[] = inbox.search(new FlagTerm( new Flags(Flag.SEEN), false));;
			
			System.out.println("Number of mails = " + messages.length);
			
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				Address[] from = message.getFrom();
				processMessageBody(message);				
				temp = DATE_FORMAT.format(message.getSentDate());				
				twList.addArrayData(temp.toString() + "," + from[0].toString() + "," + message.getSubject().toString() + "," + processMessageBody(message).toString());
			}
			inbox.close(true);
			store.close();
			
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}	
		
		for(int i= 0; i < twList.getArraySize(); i++){
			System.out.println(twList.getArrayData(i));
		}
		
		return twList;	
	}
    
    

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
	    
	    String msg = "Dear " + toAddress + "!<br>";
        msg += "<font color=#002b54>The payment request need to approve. <br><br> ";       		

        
        msg += "<p style='font-family:'helvetica neue',arial,Helvetica,sans-serif;text-decoration:none;text-align:center;font-size:14px;display:block;padding:15px 0;color:white;margin-top:10px>";
        msg += "<a style='text-decoration:none;color:white;background:#7AD385;padding:10px 15px;font-weight:bold;font-size:14px;line-height:30px;white-space:nowrap' href='google.com'>Approve</a></p>";
        
        msg += "<p style='font-family:'helvetica neue',arial,Helvetica,sans-serif;text-decoration:none;text-align:center;font-size:14px;display:block;padding:15px 0;color:white;margin-top:10px>";
        msg += "<a style='text-decoration:none;color:white;background:#BB5A48;padding:10px 15px;font-weight:bold;font-size:14px;line-height:30px;white-space:nowrap' href='google.com'>Reject</a></p>";

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


