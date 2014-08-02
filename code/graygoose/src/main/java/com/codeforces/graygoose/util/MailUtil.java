package com.codeforces.graygoose.util;

import org.nocturne.util.StringUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailUtil {
    private static final Properties properties;
    private static final Session session;

    public static void sendMail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new Date());

        Transport.send(message, message.getRecipients(Message.RecipientType.TO));
    }

    static {
        properties = new Properties();

        properties.put("mail.smtp.host", "codeforces.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.from", "graygoose@codeforces.com");

        if (StringUtil.isEmptyOrNull(properties.getProperty("mail.user"))) {
            session = Session.getInstance(properties);
        } else {
            session = Session.getInstance(properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(properties
                                    .get("mail.user").toString(), properties
                                    .get("mail.password").toString());
                        }
                    });
        }
    }

    private MailUtil() {
    }
}