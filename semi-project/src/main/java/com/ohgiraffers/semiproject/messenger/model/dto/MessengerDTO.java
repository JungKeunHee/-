package com.ohgiraffers.semiproject.messenger.model.dto;

import com.ohgiraffers.semiproject.employee.model.dto.DeptDTO;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeJoinListDTO;
import com.ohgiraffers.semiproject.employee.model.dto.JobDTO;
import com.ohgiraffers.semiproject.schedule.model.dto.ScheduleDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessengerDTO {

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