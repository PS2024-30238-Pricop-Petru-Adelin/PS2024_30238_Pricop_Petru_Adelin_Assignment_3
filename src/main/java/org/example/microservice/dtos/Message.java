package org.example.microservice.entity;

import com.olxapplication.dtos.UserMailDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String id;

    private String msg;

    private UserMailDTO sender;

    private UserMailDTO receiver;

    private LocalDateTime date;
}
