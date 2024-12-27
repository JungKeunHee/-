package com.ohgiraffers.semiproject.manager.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacationManagementDTO {
    private List<String> empCodes; // 사번 리스트
    private String leaveType; // 연차/휴가 유형
    private int leaveDays; // 일수
}
