package client;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class Trimite{
	static String cale;
	public static void ataseaza(File f){cale=f.getPath();}	
	public static void trimite(String dela,String la,String subiect,String porc,String pass) {			
	Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(dela, pass);
      }
    });
    try {
    	 MimeMessage message = new MimeMessage(session);  
    	    message.setFrom(new InternetAddress(dela));  
    	    message.addRecipient(Message.RecipientType.TO,new InternetAddress(la));  
    	    message.setSubject(subiect);      	      
    	    //3) create MimeBodyPart object and set your message text     
    	    BodyPart messageBodyPart1 = new MimeBodyPart();  
    	    messageBodyPart1.setText(porc);      	      
    	    //4) create new MimeBodyPart object and set DataHandler object to this object          	      
    	    Multipart multipart = new MimeMultipart();
    	    if (cale!=null){
    	    	MimeBodyPart messageBodyPart2 = new MimeBodyPart();
    	    	DataSource source = new FileDataSource(cale);  
    	    messageBodyPart2.setDataHandler(new DataHandler(source));  
    	    messageBodyPart2.setFileName(cale);
    	    multipart.addBodyPart(messageBodyPart2); 
    	    }
    	    //5) create Multipart object and add MimeBodyPart objects to this object          	      
    	    multipart.addBodyPart(messageBodyPart1);  
    	    message.setFrom(new InternetAddress("itstimetotimein@gmail.com","TimeIn")); 
    	    
    	    //6) set the multiplart object to the message object  
    	    message.setContent(multipart );  
 
    	 
    	   Transport.send(message);	   
    	   System.out.println("Rezolvat.");  
    	   }catch (MessagingException | UnsupportedEncodingException ex) {ex.printStackTrace();}  
    	 }
}