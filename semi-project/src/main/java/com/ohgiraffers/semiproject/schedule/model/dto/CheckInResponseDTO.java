package com.ohgiraffers.semiproject.schedule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckInResponseDTO {
    private int workCode;
    private String empCode;
    private LocalDateTime checkInTime;
}
