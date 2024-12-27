package com.ohgiraffers.semiproject.home.auth.model.service;

import com.ohgiraffers.semiproject.home.auth.model.dto.AuthDetailes;
import com.ohgiraffers.semiproject.home.model.dto.LoginUserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final MemberService memberService;

    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUserDTO login = memberService.findByUsername(username);

        if (login == null) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다");
        }

        return new AuthDetailes(login);
    }

}
