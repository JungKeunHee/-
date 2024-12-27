package com.ohgiraffers.semiproject.schedule.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceRequest {

    private String date;
    private String type;
}
