package com.ohgiraffers.semiproject.home.model.service;

import com.ohgiraffers.semiproject.home.model.dao.UserMapper;
import com.ohgiraffers.semiproject.home.model.dto.NoCheckDTO;
import com.ohgiraffers.semiproject.home.model.dto.NoSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoCheckService {

    @Autowired
    private UserMapper userMapper;

    public NoCheckDTO noCheck(NoSearchDTO noCheckDTO) {

        NoCheckDTO userInfo = userMapper.findByNo(noCheckDTO);

        System.out.println("userInfo = " + userInfo);

        return userInfo;
    }
}
