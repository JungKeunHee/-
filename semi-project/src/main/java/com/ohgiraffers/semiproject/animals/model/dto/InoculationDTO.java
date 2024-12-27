package com.ohgiraffers.semiproject.animals.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InoculationDTO {

    private String inoculationCode;
    private String inoculationName;

    public InoculationDTO(String healthcheckCode) {
        this.inoculationCode = healthcheckCode;
    }

}
