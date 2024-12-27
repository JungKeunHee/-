package com.ohgiraffers.semiproject.schedule.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckOutRequestDTO {

    private String date;
    private String endTime;
    private int totalHours; // 총 근무 시간 (시간 단위)
    private int totalMinutes; // 총 근무 시간 (분 단위)
    private int totalSeconds; // 총 근무 시간 (초 단위)
}
