package com.ohgiraffers.semiproject.messenger.model.dao;

import com.ohgiraffers.semiproject.messenger.model.dto.MessengerDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessengerMapper {
    List<MessengerDTO> findEmployees();
}
