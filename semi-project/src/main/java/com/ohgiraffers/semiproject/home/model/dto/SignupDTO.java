package com.ohgiraffers.semiproject.home.model.dto;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignupDTO {
    private String code;
    private String pass;
    private String name;
    private String email;
    private String phone;
    private Date hireDate;
    private String gender;
    private String address;
    private String jobCode;
    private String deptCode;
    private String authority;
    private String image;
}
