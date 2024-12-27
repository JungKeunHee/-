package com.ohgiraffers.semiproject.mail.model.dao;

import com.ohgiraffers.semiproject.mail.model.dto.MailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MailMapper {
    List<MailDTO> mailAllSelect(String code, @Param("offset") int offset, @Param("size") int size);

    MailDTO mailDetail(Integer emailCode);

    List<MailDTO> mailSentSelect(String code, @Param("offset") int offset, @Param("size") int size);

    List<MailDTO> mailFolderImportant(String code, @Param("offset") int offset, @Param("size") int size);

    List<MailDTO> mailFolderTrash(String code, @Param("offset") int offset, @Param("size") int size);

    List<MailDTO> mailFolderArchived(String code, @Param("offset") int offset, @Param("size") int size);

    void moveMails(@Param("mail") List<Integer> mail, @Param("recipientFolder") String recipientFolder, @Param("code") String code);

    void sentMoveMails(@Param("mail") List<Integer> mail, @Param("senderFolder") String recipientFolder, @Param("code") String code);

    void inboxRegistMail(MailDTO mailDTO);

    void sentRegistMail(MailDTO mailDTO);

    void inboxDeleteMails(@Param("mail") List<Integer> mail, @Param("code") String code);

    void sentDeleteMails(@Param("mail") List<Integer> mail, @Param("code") String code);

    long inboxTotalProducts(@Param("code") String code);

    long sentTotalProducts(@Param("code") String code);

    long trashTotalProducts();

    long importantTotalProducts();
}

