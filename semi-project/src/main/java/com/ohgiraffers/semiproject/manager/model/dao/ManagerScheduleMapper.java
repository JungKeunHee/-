package com.ohgiraffers.semiproject.manager.model.dao;

import com.ohgiraffers.semiproject.manager.model.dto.ManagerScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerScheduleMapper {

    // 관리자에게 요청한 근태수정 목록 가져오기
    List<ManagerScheduleDTO> getAttendanceRequest(String code);

    // 출근일 경우 수정 로직
    void modifyStartTime(ManagerScheduleDTO parameter);

    // 퇴근일 경우 수정 로직
    void modifyEndTime(ManagerScheduleDTO parameter);

    // 반려일 때 수정 로직
    void modifyProgressCode(ManagerScheduleDTO parameter);
}
