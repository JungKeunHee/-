package com.ohgiraffers.semiproject.animals.model.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HealthCheckDTO {

    private String healthcheckCode;
    private String healthcheckName;

    public HealthCheckDTO(String healthcheckCode) {
        this.healthcheckCode = healthcheckCode;
    }
}
