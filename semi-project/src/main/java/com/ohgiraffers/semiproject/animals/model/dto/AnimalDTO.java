package com.ohgiraffers.semiproject.animals.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AnimalDTO {

    private String animalImage; // 동물 사진
    private String animalCode; // 동물등록번호
    private int age; // 나이
    private String gender; // 성별
    private List<HealthCheckDTO> healthChecks   ;
    private List<InoculationDTO> inoculations;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rescueDate; // 구조날짜
    private String rescueLocation; // 구조위치
    private String animalStatus; // 동물상태
    private AdoptDTO adoptDTO; // 입양자이름, 폰번호, 입양시작날짜, 입양완료날짜
    private TypeDTO typeDTO; // 분류(개, 고양이)
    private BreedDTO breedDTO; // 품종
    private EmpDTO empDTO; // 담당자(사원)번호
    private String etc; // 기타사항
}
