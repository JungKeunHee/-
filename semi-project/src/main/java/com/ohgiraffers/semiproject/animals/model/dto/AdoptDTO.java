package com.ohgiraffers.semiproject.animals.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdoptDTO {
    private String adoptNo;
    private String adoptName;
    private String adoptPhone;
    private Date adoptStartDate; // 입양시작날짜
    private Date adoptEndDate; // 입양완료날짜
}
