package com.ohgiraffers.semiproject.schedule.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacationDTO {

    private String code; // 사번코드
    private int annualLeave; // 연차
    private int vacation; // 휴가
}
