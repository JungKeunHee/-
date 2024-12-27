package com.ohgiraffers.semiproject.home.model.dto;

import com.ohgiraffers.semiproject.common.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginUserDTO {
    private String code;
    private String pass;
    private String name;
    private String email;
    private String phone;
    private Date hireDate;
    private Date entDate;
    private String gender;
    private String address;
    private String jobCode;
    private String deptCode;
    private UserRole authority;
    private String image;

    public List<String> getRole() {

        if(this.authority.getRole().length() > 0){

            return Arrays.asList(this.authority.getRole().split(","));
        }
        return new ArrayList<>();
    }
}
