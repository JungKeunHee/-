package com.ohgiraffers.semiproject.schedule.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CheckInRequestDTO {

    private String date;
    private String startTime;
}
