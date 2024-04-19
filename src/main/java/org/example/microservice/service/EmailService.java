package org.example.microservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.microservice.dtos.ResponseMessageDto;
import org.example.microservice.dtos.NotificationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private Environment environment;

    private final SpringTemplateEngine templateEngine;
    @Autowired
    private QueueListener queueListener;

    public EmailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public ResponseMessageDto sendEmail(NotificationRequestDto notificationRequestDto) {
        try {

            String subject = "";
            String body = "";
            if(notificationRequestDto.getAction().equals("update")){
                subject = "User update";
                body = "Your user details have been updated successfully: " + notificationRequestDto.getNume() + ".";
            } else
                if(notificationRequestDto.getAction().equals("insert")){
                    subject = "User added";
                    body = "You are now part of our comunity, " + notificationRequestDto.getNume() + "\uD83E\uDD73\uD83E\uDD73";
                }

            sendHtmlEmail(notificationRequestDto.getEmail(), subject, body, "User.html");
            return new ResponseMessageDto("Success", "Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseMessageDto("Failure", "Failed to send email");
        }
    }

    public void sendHtmlEmail(String to, String subject, String body, String templateName) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        Context context = new Context();
        context.setVariable("body", body);
        String process = templateEngine.process(templateName, context);
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(process, true);

        emailSender.send(message);
    }
}
