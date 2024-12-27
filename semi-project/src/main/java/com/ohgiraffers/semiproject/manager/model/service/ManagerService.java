package com.ohgiraffers.semiproject.manager.model.service;

import com.ohgiraffers.semiproject.manager.model.dao.ManagerDataMapper;
import com.ohgiraffers.semiproject.manager.model.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerDataMapper managerDataMapper;

    public ManagerService(ManagerDataMapper managerDataMapper){
        this.managerDataMapper = managerDataMapper;
    }

    // 근태수정 요청 대기중 카운트 수 가져오기
    public List<ManagerScheduleDTO> getScheduleList(String code) {
        return managerDataMapper.getScheduleList(code);
    }

    // 경조사 대기중 카운트 수 가져오기
    public List<CacPaymentDTO> getCacPaymentList(String code) {
        return managerDataMapper.getCacPaymentList(code);
    }

    // 연장 결재 대기중 카운트 수 가져오기
    public List<OverTimeDTO> getOverTimeList(String code) {
        return managerDataMapper.getOverTimeList(code);
    }

    // 퇴직원 결재 대기중 카운트 수 가져오기
    public List<RetirementDTO> getRetirement(String code) {
        return managerDataMapper.getRetirement(code);
    }

    // 연차 대기중 카운트 수 가져오기
    public List<VacPaymentDTO> getAnnualList(String code) {
        return managerDataMapper.getAnnualList(code);
    }

    // 휴가 대기중 카운트 수 가져오기
    public List<VacPaymentDTO> getVacationList(String code) {
        return managerDataMapper.getVacationList(code);
    }
}
