package com.ohgiraffers.semiproject.adoption.model.dto;

import com.ohgiraffers.semiproject.animals.model.dto.AnimalDTO;
import com.ohgiraffers.semiproject.animals.model.dto.EmpDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdoptionDTO {

    private String adoptNo;
    private String adoptName;
    private String adoptEmail;
    private String adoptPhone;
    private String adoptAge;
    private String adoptGender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String adoptStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String adoptEndDate;
    private String adoptStatus;
    private AnimalDTO animalDTO;
    private EmpDTO empDTO;
}
