package org.example.microservice.controller;

import com.olxapplication.dtos.UserMailDTO;
import org.example.microservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The controller for sending asynchronous emails.
 */
@Service
@RabbitListener(queues = "${rabbitmq.queue}", id = "listener")
public class QueueListener {
    @Autowired
    private EmailService emailService;

    /**
     * Handles the messages received from the queue.
     * @param userDto The data transfer object containing the user's details.
     */
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
