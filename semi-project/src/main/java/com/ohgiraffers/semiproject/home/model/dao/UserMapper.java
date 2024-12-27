package com.ohgiraffers.semiproject.home.model.dao;

import com.ohgiraffers.semiproject.home.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int regist(SignupDTO signupDTO);

    LoginUserDTO findByUsername(String username);

    NoCheckDTO findByNo(NoSearchDTO noCheckDTO);

    void updatePass(String code, String hashedPassword);

    PassSearchDTO selectEmail(PassSearchDTO code);

    String selectName(String empNo);
}
