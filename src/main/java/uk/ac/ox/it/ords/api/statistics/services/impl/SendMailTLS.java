/*
 * Copyright 2015 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import org.apache.commons.configuration.ConfigurationConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.configuration.MetaConfiguration;
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
		props = ConfigurationConverter.getProperties(MetaConfiguration.getConfiguration());
    }

	@Override
	public void sendMessage(String message) {
		sendStatsMail(message);		
	}
    
    private void sendStatsMail(String messageBody) {
    	messageText = messageBody;
    	email = props.getProperty("mail.smtp.to");
    	subject = props.getProperty("mail.smtp.subject");
    	username = props.getProperty("mail.smtp.username");
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
