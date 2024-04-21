package org.example.microservice.controller;

import org.example.microservice.dtos.NotificationRequestDto;
import org.example.microservice.dtos.ResponseMessageDto;
import org.example.microservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/microservice")
public class Controller {
    @Autowired
    EmailService emailService;

    @PostMapping("/receiver")
    public ResponseEntity<ResponseMessageDto> sendEmail(@RequestHeader HttpHeaders headers, @RequestBody NotificationRequestDto notificationRequestDto) {
        if(notificationRequestDto.getEmail() == null || notificationRequestDto.getEmail().isEmpty()) {
            return ResponseEntity.ok(new ResponseMessageDto("failure", "Missing email address"));
        }

        String authorizationHeader = headers.getFirst("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String token = authorizationHeader.substring(7);

            if (token.equals(notificationRequestDto.getId() + notificationRequestDto.getEmail())) {
                ResponseMessageDto messageDto = emailService.sendEmail(notificationRequestDto);
                return ResponseEntity.ok(messageDto);
            }
        }
        return ResponseEntity.ok(new ResponseMessageDto("failure", "Invalid token"));
    }
}
