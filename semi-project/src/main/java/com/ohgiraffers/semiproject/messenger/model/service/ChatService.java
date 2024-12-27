package com.ohgiraffers.semiproject.messenger.model.service;

import com.ohgiraffers.semiproject.messenger.model.dao.ChatMapper;
import com.ohgiraffers.semiproject.messenger.model.dto.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMapper mapper;



    public List<ChatDTO> getChatHistory(String senderCode, String receiverCode) {
        return mapper.findChatHistory(senderCode, receiverCode); // DB에서 사용자 간 채팅 기록 조회
    }

    public void insertChat(ChatDTO chat, String senderCode) {
        mapper.save(chat, senderCode);
    }


//    public List<Chat> findBySenderCodeAndReceiverCode(String sender, String receiver) {
//
//        return mapper.findByChatList(sender, receiver);
//
//    }
}
