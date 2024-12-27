package com.ohgiraffers.semiproject.schedule.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckOutResponseDTO {
    private int workCode;
    private String empCode;
    private LocalDateTime checkOutTime;
}
