package org.example.microservice.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMessageDto {
    private String status;
    private String message;
}