package com.ohgiraffers.semiproject.manager.model.service;

import com.ohgiraffers.semiproject.manager.model.dao.ManagerScheduleMapper;
import com.ohgiraffers.semiproject.manager.model.dto.ManagerScheduleDTO;
import com.ohgiraffers.semiproject.schedule.model.dto.ScheduleDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerScheduleService {

    private final ManagerScheduleMapper managerScheduleMapper;

    public ManagerScheduleService(ManagerScheduleMapper managerScheduleMapper){
        this.managerScheduleMapper = managerScheduleMapper;
    }

    // 관리자에게 요청한 근태수정 목록 불러오기
    public List<ManagerScheduleDTO> getAttendanceRequest(String code) {

        return managerScheduleMapper.getAttendanceRequest(code);
    }

    // 출근일 경우 수정 로직
    @Transactional
    public void modifyStartTime(ManagerScheduleDTO parameter) {

        managerScheduleMapper.modifyStartTime(parameter);
    }

    // 퇴근일 경우 수정 로직
    @Transactional
    public void modifyEndTime(ManagerScheduleDTO parameter) {

        managerScheduleMapper.modifyEndTime(parameter);
    }

    // 반려일 경우 수정 로직
    @Transactional
    public void modifyProgressCode(ManagerScheduleDTO parameter) {

        managerScheduleMapper.modifyProgressCode(parameter);
    }
}
