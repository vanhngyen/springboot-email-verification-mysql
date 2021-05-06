package com.example.account.service;

import com.example.account.model.MailProperties;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SendingMailService {
    private final MailProperties mailProperties;
    private final Configuration templates;

    @Autowired
    SendingMailService (MailProperties mailProperties, Configuration templates) {
        this.mailProperties = mailProperties;
        this.templates = templates;
    }

    public boolean SendVerificationMail (String toEmail, String verificationCode) throws UnsupportedEncodingException, MessagingException {
        String subject = "Please verify your email";
        String body = "";
        try {
            Template t = templates.getTemplate("email-verification.ftl");
            Map<String, String> map = new HashMap<>();
            map.put("VERIFICATION_URL", mailProperties.getVerificationapi() + verificationCode);
            body = FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
        } catch( Exception e ) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return sendMail(toEmail, subject, body);

    }

    private boolean sendMail (String toEmail, String subject, String body) throws UnsupportedEncodingException, MessagingException {
        try {

            Properties properties = System.getProperties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.port", mailProperties.getStmp().getPort());
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(properties);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getFromName()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            Transport transport = session.getTransport();
            transport.connect(mailProperties.getStmp().getHost(), mailProperties.getStmp().getUsername()
                , mailProperties.getStmp().getPassword());
            transport.sendMessage(msg, msg.getAllRecipients());
            return true;
        } catch( Exception e ) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,e.getMessage(),e);
        }
        return false;
    }
}
