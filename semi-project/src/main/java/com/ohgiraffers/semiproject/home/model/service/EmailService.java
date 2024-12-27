package com.ohgiraffers.semiproject.home.model.service;

import com.ohgiraffers.semiproject.home.model.dao.UserMapper;
import com.ohgiraffers.semiproject.home.model.dto.PassSearchDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserMapper userMapper;

    public void sendTemporaryPasswordEmail(String to, String temporaryPassword) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("[PetHarmony] 임시 비밀번호 발급");

            // HTML 형식의 내용 설정
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;'>" +
                    "<div style='max-width: 600px; margin: 40px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 20px rgba(0,0,0,0.1);'>" +
                    "<h1 style='color: #333; font-size: 24px;'>안녕하세요, PetHarmony 입니다!</h1>" +
                    "<br>" +
                    "<p style='font-size: 16px; color: #555; line-height: 1.5;'>임시 비밀번호가 발급되었습니다. 아래의 비밀번호로 로그인 해주세요:</p>" +
                    "<div style='font-size: 20px; font-weight: bold; color: #007BFF; padding: 10px; border: 1px solid #007BFF; border-radius: 5px; display: inline-block; margin: 20px 0;'>" + temporaryPassword + "</div>" +
                    "<p style='font-size: 16px; color: #555; line-height: 1.5;'>임시 비밀번호는 마이페이지에서 꼭 비밀번호를 수정해주시길 바랍니다. 감사합니다.</p>" +
                    "<a href='http://localhost:9595/home' style='display: inline-block; padding: 10px 20px; background-color: #007BFF; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px;'>로그인하기</a>" +
                    "<div style='margin-top: 30px; font-size: 12px; color: #aaa; text-align: center;'>이 메일은 자동으로 발송된 메일입니다. 회신하지 마세요.</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // true로 설정하여 HTML 형식으로 전송

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // 예외 처리
        }

    }

    @Transactional
    public void updatePassword(String code, String hashedPassword) {

        userMapper.updatePass(code, hashedPassword);
    }

    public PassSearchDTO findEmailByCode(PassSearchDTO code) {

        PassSearchDTO passSearchDTO = userMapper.selectEmail(code);

        return passSearchDTO;
    }
}
