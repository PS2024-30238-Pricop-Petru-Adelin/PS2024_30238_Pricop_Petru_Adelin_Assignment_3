package org.example.microservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationRequestDto {
    private String id;
    private String nume;
    private String email;
    private String action;
    private String filePath;
}
