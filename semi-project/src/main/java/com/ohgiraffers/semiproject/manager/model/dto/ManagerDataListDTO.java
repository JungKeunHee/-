package com.ohgiraffers.semiproject.manager.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ManagerDataListDTO {

    private List<ManagerScheduleDTO> scheduleList;
    private List<CacPaymentDTO> cacPaymentList;
    private List<OverTimeDTO> overTimeList;
    private List<RetirementDTO> retirementList;
    private List<VacPaymentDTO> annualPaymentList; // 연차
    private List<VacPaymentDTO> vacationPaymentList; // 휴가

}
