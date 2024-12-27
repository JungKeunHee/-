package com.ohgiraffers.semiproject.employee.model.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeJoinListDTO {
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
}
