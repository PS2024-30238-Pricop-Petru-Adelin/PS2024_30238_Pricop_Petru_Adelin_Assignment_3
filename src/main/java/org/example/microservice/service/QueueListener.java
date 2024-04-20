package org.example.microservice.service;

import com.olxapplication.dtos.UserMailDTO;
import org.example.microservice.config.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "${rabbitmq.queue}", id = "listener")
public class QueueListener {
    @Autowired
    private EmailService emailService;

    @RabbitHandler
    public void listen(UserMailDTO userDto) {

        if (userDto.getFirstName() != null && userDto.getLastName() != null && userDto.getEmail() != null) {
            emailService.sendEmail(userDto);
            System.out.println("Received message from queue: " + userDto.toString());
        } else {
            System.out.println("Received message with missing fields from queue: " + userDto);
        }
    }
}
