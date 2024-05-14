package org.example.microservice.service;

import com.olxapplication.dtos.UserMailDTO;
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

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Service class for sending emails in microservice.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private Environment environment;

    private final SpringTemplateEngine templateEngine;

    public EmailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    /**
     * Sends an email received synchronously based on the action specified in the NotificationRequestDto(insert, update, report).
     * @param notificationRequestDto The notificationRequestDto object containing the necessary data for sending the email.
     */
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

            sendHtmlEmail(notificationRequestDto.getEmail(), subject, body, "User.html", notificationRequestDto.getFilePath());
            return new ResponseMessageDto("Success", "Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseMessageDto("Failure", "Failed to send email");
        }
    }

    /**
     * Sends an email received asynchronously based on the action specified in the UserMailDTO(insert, update, report).
     * @param userMailDTO The userMail object containing the necessary data for sending the email.
     */
    public void sendEmail(UserMailDTO userMailDTO) {
        try {

            String subject = "";
            String body = "";
            if(userMailDTO.getAction().equals("update")){
                subject = "User update";
                body = "Your user details have been updated successfully: " +  userMailDTO.getFirstName() + " " + userMailDTO.getLastName() + ".";
            } else
            if(userMailDTO.getAction().equals("insert")){
                subject = "User added";
                body = "You are now part of our comunity, " + userMailDTO.getFirstName() + " " + userMailDTO.getLastName()+ "\uD83E\uDD73\uD83E\uDD73";
            }
            if(userMailDTO.getAction().equals("report")){
                subject = "Report";
                body = "Your report is here, " + userMailDTO.getFirstName() + " " + userMailDTO.getLastName()+ "\uD83D\uDCC4";
            }

            sendHtmlEmail(userMailDTO.getEmail(), subject, body, "User.html", userMailDTO.getFilePath());
            return ;

        } catch (MessagingException e) {
            e.printStackTrace();
            return ;
        }
    }

    /**
     * Sends an HTML email with the specified subject and body.
     * If a file path is provided, the file is attached to the email.
     * @param to The recipient of the email.
     * @param subject The subject of the email.
     * @param body The body of the email.
     * @param templateName The name of the Thymeleaf template to use for the email.
     * @param filePath The path of the file to attach to the email.
     * @throws MessagingException If there is an error creating or sending the email.
     */
    public void sendHtmlEmail(String to, String subject, String body, String templateName, String filePath) throws MessagingException {
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
        if(!filePath.equals("")) {
            File f = new File(filePath);
            helper.addAttachment("Your Report", f);
        }

        emailSender.send(message);
    }
}
