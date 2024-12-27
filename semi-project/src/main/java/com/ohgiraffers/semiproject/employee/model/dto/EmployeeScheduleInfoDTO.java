package com.ohgiraffers.semiproject.employee.model.dto;

import com.ohgiraffers.semiproject.schedule.model.dto.ScheduleDTO;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeScheduleInfoDTO {
    private String empCode;
    private String empPass;
    private String empName;
    private String empEmail;
    private String empPhone;
    private Date hireDate;
    private Date entDate;
    private String empGender;
    private String empAddress;
    private String jobCode;
    private String deptCode;
    private String authority;
    private String profile_image;
    private DeptDTO deptDTO;
    private JobDTO jobDTO;
    private ScheduleDTO scheduleDTO;
}
