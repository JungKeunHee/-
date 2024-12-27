package com.ohgiraffers.semiproject.approval.model.service;

import com.ohgiraffers.semiproject.approval.model.dao.ApprovalMapper;
import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.OverTimeDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalService {

    private final ApprovalMapper approvalMapper;

    @Autowired
    public ApprovalService (ApprovalMapper approvalMapper){
        this.approvalMapper = approvalMapper;
    }

    @Transactional
    public void insertVacForm(VacPaymentDTO vacPaymentDTO) {
        approvalMapper.insertVacForm(vacPaymentDTO);
    }

    @Transactional
    public void insertCacForm(CacPaymentDTO cacPaymentDTO) {
        approvalMapper.insertCacForm(cacPaymentDTO);
    }

    @Transactional
    public void insertOverTimeForm(OverTimeDTO overTimeDTO) {
        approvalMapper.insertOverTimeForm(overTimeDTO);
    }

    @Transactional
    public void insertRetireMentForm(RetirementDTO retirementDTO) {
        approvalMapper.insertRetireMentForm(retirementDTO);
    }

    public List<CacPaymentDTO> getMyCacPaymentList(String code) {
        return approvalMapper.getMyCacPaymentList(code);
    }

    public List<OverTimeDTO> getMyOverTimeList(String code) {
        return approvalMapper.getMyOverTimeList(code);
    }

    public List<RetirementDTO> getMyRetirement(String code) {
        return approvalMapper.getMyRetirement(code);
    }

    public List<VacPaymentDTO> getMyVacPayment(String code) {
        return approvalMapper.getMyVacPayment(code);
    }

    // 경조사 데이터 삭제하기
    @Transactional
    public void deleteCacProcess(int no) {
        approvalMapper.deleteCacProcess(no);
    }

    // 연장근무 데이터 삭제하기
    public void deleteOverTimeProcess(int no) {
        approvalMapper.deleteOverTimeProcess(no);
    }

    // 퇴직서 데이터 삭제하기
    public void deleteRetirementProcess(int no) {
        approvalMapper.deleteRetirementProcess(no);
    }

    // 연차 휴가 데이터 삭제하기
    public void deleteVacationProcess(int no) {
        approvalMapper.deleteVacationProcess(no);
    }
}
