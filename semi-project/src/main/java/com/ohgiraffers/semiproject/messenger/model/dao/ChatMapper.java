package com.ohgiraffers.semiproject.messenger.model.dao;

import com.ohgiraffers.semiproject.messenger.model.dto.ChatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {


    List<ChatDTO> findChatHistory(@Param("senderCode") String senderCode, @Param("receiverCode") String receiverCode);

    void save(ChatDTO chat, String senderCode);


//    List<Chat> findByChatList(@Param("sender") String sender, @Param("receiver") String receiver);
}