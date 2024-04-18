package org.example.microservice.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.microservice.entity.Mail;
import org.example.microservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
@AllArgsConstructor
@RabbitListener(queues = "rabbitmq.queue", id = "listener")
public class RabbitMQReceiver {
    private static Logger logger = LogManager.getLogger(RabbitMQReceiver.class.toString());
    EmailService emailService;
    @RabbitHandler
    public void receiver(String mailString) {
        logger.info("MenuOrder listener invoked - Consuming Message with MenuOrder Identifier : " + mailString);
        String[] str = mailString.split("#");
        Mail mail = new Mail(
                str[0],
                str[1],
                str[2]
        );
        System.out.println(str[0]);
        System.out.println(str[1]);
        System.out.println(str[2]);
        try {
            emailService.sendHtmlEmail(mail.getTo(), mail.getSubject(), mail.getBody(), "UserUpdate");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}