package com.ohgiraffers.semiproject.manager.model.service;

import com.ohgiraffers.semiproject.manager.model.dao.ManagerApprovalMapper;
import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.OverTimeDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerApprovalService {

    @Autowired
    private final ManagerApprovalMapper managerApprovalMapper;

    public ManagerApprovalService(ManagerApprovalMapper managerApprovalMapper){
        this.managerApprovalMapper = managerApprovalMapper;
    }

    public List<CacPaymentDTO> getCacPaymentList(String code) {
        return managerApprovalMapper.getCacPaymentList(code);
    }

    public List<OverTimeDTO> getOverTimeList(String code) {
        return managerApprovalMapper.getOverTimeList(code);
    }

    public List<RetirementDTO> getRetirement(String code) {
        return managerApprovalMapper.getRetirement(code);
    }

    public List<VacPaymentDTO> getVacPayment(String code) {
        return managerApprovalMapper.getVacPayment(code);
    }

    public VacPaymentDTO getVacRequest(String documentNo) {

        return managerApprovalMapper.getVacRequest(documentNo);
    }

    @Transactional
    public void vacationProcess(VacPaymentDTO process) {
        managerApprovalMapper.vacationProcess(process);
    }

    public VacPaymentDTO getVacationDetails(String documentNo) {
        return managerApprovalMapper.getVacationDetails(documentNo);
    }

    public VacPaymentDTO getDate(int documentNo) {
        return managerApprovalMapper.getDate(documentNo);
    }



    public CacPaymentDTO getCacRequest(String documentNo) {

        return managerApprovalMapper.getCacRequest(documentNo);
    }

    public CacPaymentDTO getCacDetails(String documentNo) {
        return managerApprovalMapper.getCacDetails(documentNo);
    }

    @Transactional
    public void cacProcess(CacPaymentDTO process) {
        managerApprovalMapper.cacProcess(process);
    }


    public OverTimeDTO getOverTimeRequest(String documentNo) {
        return managerApprovalMapper.getOverTimeRequest(documentNo);
    }

    public OverTimeDTO getOverTimeDetails(String documentNo) {
        return managerApprovalMapper.getOverTimeDetails(documentNo);
    }

    @Transactional
    public void overTimeProcess(OverTimeDTO process) {
        managerApprovalMapper.overTimeProcess(process);
    }

    public RetirementDTO getRetirementRequest(String documentNo) {
        return managerApprovalMapper.getRetirementRequest(documentNo);
    }

    public RetirementDTO getRetirementDetails(String documentNo) {
        return managerApprovalMapper.getRetirementDetails(documentNo);
    }

    @Transactional
    public void retirementProcess(RetirementDTO process) {
        managerApprovalMapper.retirementProcess(process);
    }
}
