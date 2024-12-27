package com.ohgiraffers.semiproject.home.auth.model.service;

import com.ohgiraffers.semiproject.home.model.dao.UserMapper;
import com.ohgiraffers.semiproject.home.model.dto.LoginUserDTO;
import com.ohgiraffers.semiproject.home.model.dto.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public int regist(SignupDTO signupDTO) {

        // 비밀번호 암호화
        signupDTO.setPass(encoder.encode(signupDTO.getPass()));

        int result = userMapper.regist(signupDTO);

        return result;
    }

    public LoginUserDTO findByUsername(String username) {

        LoginUserDTO login = userMapper.findByUsername(username);

        if (login == null){
            return null;
        } else {
            return login;
        }
    }
}
