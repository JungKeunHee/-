package com.ohgiraffers.semiproject.home.controller;

import com.ohgiraffers.semiproject.home.model.dto.PassSearchDTO;
import com.ohgiraffers.semiproject.home.model.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@Controller
@RequestMapping("/home/*")
public class PassSearchController {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    @Autowired
    private EmailService emailService; // EmailService 주입

    // BCryptPasswordEncoder 인스턴스 생성
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("pass-search")
    @ResponseBody // JSON 응답을 반환하기 위해 추가
    public ResponseEntity<String> passSearch(@ModelAttribute PassSearchDTO passSearchDTO) {

        // 사번으로 이메일 조회
        PassSearchDTO registeredEmail = emailService.findEmailByCode(passSearchDTO);

        // 입력된 이메일과 조회된 이메일 비교
        if (registeredEmail == null || registeredEmail.getEmail() == null || !registeredEmail.getEmail().equals(passSearchDTO.getEmail())) {
            // 이메일이 일치하지 않거나 사번에 해당하는 이메일이 없는 경우
            return ResponseEntity.badRequest().body("일치하는 정보가 없습니다. 다시 한 번 확인해주세요.");
        }

        // 임시 비밀번호 생성
        String temporaryPassword = generateTemporaryPassword();

        // 해싱된 비밀번호 생성
        String hashedPassword = hashPassword(temporaryPassword);

        // 데이터베이스에 해싱된 비밀번호 저장
        emailService.updatePassword(passSearchDTO.getCode(), hashedPassword);

        // 이메일로 임시 비밀번호 전송
        emailService.sendTemporaryPasswordEmail(passSearchDTO.getEmail(), temporaryPassword);

        return ResponseEntity.ok("임시 비밀번호가 발송되었습니다.");
    }


    private String generateTemporaryPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    private String hashPassword(String password) {
        // BCrypt를 사용하여 비밀번호 해싱
        return passwordEncoder.encode(password);
    }
}
