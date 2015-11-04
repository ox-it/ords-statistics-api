/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ox.it.ords.api.statistics.services.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.services.MessagingService;

/**
 *
 * @author dave
 */
public class SendMailTLS  implements MessagingService {

    private Logger log = LoggerFactory.getLogger(SendMailTLS.class);
    
    private String username;
    private String password;
    private Properties props;
    private String messageText;
    private String subject;
    private String email;
    private boolean sendEmails = true;

    public SendMailTLS() {
        log.debug("SendMailTLS init");
        props = new Properties();
        props.put("mail.smtp.auth", "false");//true");
        props.put("mail.smtp.starttls.enable", "false");//true");
        props.put("mail.smtp.host", "localhost");//smtp.gmail.com");
        props.put("mail.smtp.port", "25");//587");
        props.put("mail.smtp.from", "daemons@sysdev.oucs.ox.ac.uk");
        //setupCredentials();
        this.sendEmails = true;
    }

	@Override
	public void sendMessage(String message) {
		sendStatsMail(message);		
	}
    
    private void sendStatsMail(String messageBody) {
    	messageText = messageBody;
    	email = "ords@it.ox.ac.uk";
    	subject = "Statistics Information from ORDS";
    	username = "Mr/Ms Admin";
    	sendMail();
    }

    private void sendMail() {
        if (sendEmails) {
            if (username == null) {
                log.error("Unable to send emails due to null user");
                return;
            }
            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

            try {
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
                message.setSubject(subject);
                message.setText(messageText);
                message.setFrom(new InternetAddress("ords@it.ox.ac.uk"));

                Transport.send(message);
                
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Sent email to %s (name %s)", email, username));
                    log.debug("with content: " + messageText);
                }

            }
            catch (MessagingException e) {
                log.error("Unable to send email to " + email + " username " + username, e);
            }
        }
    }
}
