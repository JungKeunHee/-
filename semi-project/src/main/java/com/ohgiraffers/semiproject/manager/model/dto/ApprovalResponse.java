package com.ohgiraffers.semiproject.manager.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalResponse {

    private List<CacPaymentDTO> cacPaymentList;
    private List<OverTimeDTO> overTimeList;
    private List<RetirementDTO> retirementList;
    private List<VacPaymentDTO> vacPaymentList;
}
