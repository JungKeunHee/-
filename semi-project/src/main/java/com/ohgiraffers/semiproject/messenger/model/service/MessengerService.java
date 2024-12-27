package com.ohgiraffers.semiproject.messenger.model.service;

import com.ohgiraffers.semiproject.messenger.model.dao.MessengerMapper;
import com.ohgiraffers.semiproject.messenger.model.dto.MessengerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessengerService {
    @Autowired
    private MessengerMapper mapper;

    public List<MessengerDTO> findEmployees() {
        return mapper.findEmployees();
    }
}
