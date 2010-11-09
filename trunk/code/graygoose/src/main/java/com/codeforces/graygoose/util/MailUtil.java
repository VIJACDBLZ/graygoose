package com.codeforces.graygoose.util;

import org.nocturne.util.StringUtil;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MailUtil {
    private static final Properties properties;

    public static void sendMail(String to, String subject, String text) throws MessagingException {
        Session session = Session.getDefaultInstance(new Properties(), null);

        MimeMessage message = new MimeMessage(session);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new Date());
        Transport.send(message);
    }

    static {
        properties = new Properties();

        try {
            properties.load(MailUtil.class.getResourceAsStream("/mail.properties"));

            if (StringUtil.isEmptyOrNull(properties.getProperty("mail.from"))) {
                throw new IOException("Can't find mail.from.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read from properties file.", e);
        }
    }

    private MailUtil() {
    }
}