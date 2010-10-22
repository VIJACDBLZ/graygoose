package com.codeforces.graygoose.util;

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
    private final static Properties properties;

    public static String escapeTags(String html) {
        return html
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("\t", "    ").replaceAll("\n", "<br/>")
                .replaceAll(" ", "&nbsp;");
    }

    public static boolean sendMail(String to, String subject, String text) {
        if (properties.get("mail.smtp.host") == null ||
                "".equals(properties.get("mail.smtp.host"))) {
            return true;
        }

        Session session = Session.getInstance(properties, null);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(text, "text/html; charset=UTF8");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    static {
        properties = new Properties();
        try {
            properties.load(MailUtil.class.getResourceAsStream("/mail.properties"));
        } catch (IOException e) {
            System.err.println("Error: can't read from properties file: " + e.getMessage());
        }
    }
}