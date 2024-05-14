package org.example.microservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mail {
    private String to;
    private String subject;
    private String body;

    @Override
    public String toString() {
        return to + '|' + subject + '|' + body ;
    }
}
