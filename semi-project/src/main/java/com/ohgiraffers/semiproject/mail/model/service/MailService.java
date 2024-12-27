package com.ohgiraffers.semiproject.mail.model.service;

import com.ohgiraffers.semiproject.mail.model.dao.MailMapper;
import com.ohgiraffers.semiproject.mail.model.dto.MailDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MailService {

    private final MailMapper mailMapper;

    public MailService(MailMapper mailMapper) {
        this.mailMapper = mailMapper;
    }

    public List<MailDTO> mailAllSelect(String code, int offset, int size) {
        System.out.println("Service - Mail List Request: Code=" + code + ", Offset=" + offset + ", Size=" + size);
        return mailMapper.mailAllSelect(code, offset, size);
    }

    public MailDTO mailDetail(Integer emailCode) {

        return mailMapper.mailDetail(emailCode);
    }

    public List<MailDTO> mailSentSelect(String code, int offset, int size) {

        return mailMapper.mailSentSelect(code, offset, size);
    }

    public List<MailDTO> mailFolderImportant(String code, int offset, int size) {

        return mailMapper.mailFolderImportant(code, offset, size);
    }

    public List<MailDTO> mailFolderTrash(String code, int offset, int size) {

        return mailMapper.mailFolderTrash(code, offset, size);
    }

    public List<MailDTO> mailFolderArchived(String code, int offset, int size) {

        return mailMapper.mailFolderArchived(code, offset, size);
    }

    @Transactional
    public void moveMails(List<Integer> mail, String recipientFolder, String code) {

        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException("메일 ID 목록이 비어 있습니다.");
        }

        mailMapper.moveMails(mail, recipientFolder, code);
    }

    @Transactional
    public void sentMoveMails(List<Integer> mail, String senderFolder, String code) {

        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException("메일 ID 목록이 비어 있습니다.");
        }

        mailMapper.sentMoveMails(mail, senderFolder, code);
    }

    @Transactional
    public void inboxRegistMail(MailDTO mailDTO, List<String> recipientId, List<String> recipientName) {

        for (int i = 0; i < recipientId.size(); i++) {
            mailDTO.setRecipientId(recipientId.get(i));

            mailDTO.setRecipientName(recipientName.get(i));

            mailMapper.inboxRegistMail(mailDTO);
        }
    }

    @Transactional
    public void sentRegistMail(MailDTO mailDTO, List<String> recipientId, List<String> recipientName) {

        for (int i = 0; i < recipientId.size(); i++) {

            mailDTO.setRecipientId(recipientId.get(i));

            mailDTO.setRecipientName(recipientName.get(i));

            mailMapper.sentRegistMail(mailDTO);
        }
    }

    @Transactional
    public void inboxDeleteMails(List<Integer> mailIds, String userCode) {

        if (mailIds == null || mailIds.isEmpty()) {
            throw new IllegalArgumentException("메일 ID 목록이 비어 있습니다.");
        }

        if (userCode == null || userCode.isEmpty()) {
            throw new IllegalArgumentException("사용자 코드가 유효하지 않습니다.");
        }

        mailMapper.inboxDeleteMails(mailIds, userCode);

    }

    @Transactional
    public void sentDeleteMails(List<Integer> mailIds, String userCode) {

        if (mailIds == null || mailIds.isEmpty()) {
            throw new IllegalArgumentException("메일 ID 목록이 비어 있습니다.");
        }

        if (userCode == null || userCode.isEmpty()) {
            throw new IllegalArgumentException("사용자 코드가 유효하지 않습니다.");
        }

        mailMapper.sentDeleteMails(mailIds, userCode);
    }

    public long inboxTotalProducts(String code) {
        System.out.println("Service - Inbox Total Products Request: Code=" + code);
        long result = mailMapper.inboxTotalProducts(code);
        System.out.println("Total Products: " + result);  // 쿼리 결과 출력
        return result;
    }

    public long sentTotalProducts(String code) {
        return mailMapper.sentTotalProducts(code);
    }

    public long trashTotalProducts() {
        return mailMapper.trashTotalProducts();
    }

    public long importantTotalProducts() {
        return mailMapper.importantTotalProducts();
    }

    public long archivedTotalProducts() {
        return mailMapper.trashTotalProducts();
    }
}



