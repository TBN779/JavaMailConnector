package com.javamail;

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
import javax.mail.search.FlagTerm;

import org.jdom.Document;
import org.jdom.Element;

import teamworks.TWList;
import teamworks.TWObjectFactory;

/*
 * @author TungNguyen
 * */
public class ReadMailToReturnXMLType  {
    Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;
    private String returnChecker="";  
    String fromId;
    String toId;
    String returncheck = "";
    
    public ReadMailToReturnXMLType(){}
    
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

    public TWList retrieveMail(final String userName, final String passWord, final String keyword) throws Exception {
		
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
				
				if(message.getSubject().toString().contains(keyword)){		
					
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
    
    
    public Element retrieveAllMails(final String userName, final String passWord) throws Exception {
		
		String temp;
		int temp1;
		Element resultSet = new Element("resultSet");
		
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
			
			Message messages[] = inbox.search(new FlagTerm( new Flags(Flag.SEEN), false));
			temp1 = messages.length;
			String numberOfMail = Integer.toString(temp1);
			
			
			resultSet.setAttribute("recordCount", numberOfMail);
			resultSet.setAttribute("columnCount", "-1");
			Document doc = new Document(resultSet);
			doc.setRootElement(resultSet);
			
			
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				Address[] from = message.getFrom();
				processMessageBody(message);				
				temp = DATE_FORMAT.format(message.getSentDate());	
				
				
				Element record = new Element("record");
				doc.getRootElement().addContent(record);

				Element column = new Element("column");
				record.addContent(column.setText(temp.toString()));
				column.setAttribute("name", "MSG_DATE");
				
				Element column1 = new Element("column");
				record.addContent(column1.setText(from[0].toString()));
				column1.setAttribute("name", "MSG_FROM");
				
				Element column2 = new Element("column");
				record.addContent(column2.setText(message.getSubject().toString()));
				column2.setAttribute("name", "MSG_SUBJECT");
				
				Element column3 = new Element("column");
				record.addContent(column3.setText(processMessageBody(message).toString()));
				column3.setAttribute("name", "MSG_CONTENT");					
				
			}
			
			inbox.close(true);
			store.close();
			
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}	
		
		return resultSet;	
	}
    
    
}


