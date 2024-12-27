package com.ohgiraffers.semiproject.schedule.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScheduleDTO {
    private int workCode;
    private String empCode;
    private String workType;
    private LocalDateTime workStartTime;
    private LocalDateTime workEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date workDate; // Date 유지

    private LocalDateTime modifyTime;
    private String division;
    private String workModifyReason;
    private String managerIdea;
    private String progressCode;
    private String adminCode;
}
