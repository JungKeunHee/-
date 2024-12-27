package com.ohgiraffers.semiproject.home.auth.model.dto;

import com.ohgiraffers.semiproject.home.model.dto.LoginUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthDetailes implements UserDetails {

    // 로그인 시 사용하는 사용자 정보 DTO 를 필드로 갖는다.
    private LoginUserDTO loginUserDTO;

    /* comment.
     *   권한 정보를 반환하는 메소드이다. 다중 권한을 위해 Collection 타입으로
     *   return 타입이 지정되어 있으며, 사용자의 권한 정보를 넣을 때
     *   사용되는 메소드이다.
     * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        loginUserDTO.getRole().forEach(role -> authorities.add(() -> role));
        // 여러 권한을 담은 authorities 반환
        return authorities;
    }

    /* comment.
     *   사용자의 비밀번호를 반환하는 메소드 이다.
     *   UsernamePasswordAuthenticationToken 과 사용자의 비밀번호를
     *   비교할 때 사용된다.
     *  */
    @Override
    public String getPassword() {
        return loginUserDTO.getPass();
    }

    /* comment.
     *   사용자의 아이디를 반환하는 메소드이다.
     *   UsernamePasswordAuthenticationToken 과 사용자의 아이디를
     *   비교할 때 사용이 된다.
     *  */
    @Override
    public String getUsername() {
        return loginUserDTO.getCode();
    }

    /* comment.
     *   계정의 만료 여부를 표현하는 메소드이다.
     *   return 값이 false 이면 해당하는 계정을 사용할 수 없게 된다.
     *  */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /* comment.
     *   잠겨있는 계정을 확인하는 메소드
     *   false 이면 해당 계정이 잠겨 사용이 불가능 하다.
     *   ex) 비밀번호 반복 실패 시 일시적인 lock or 오랜기간 비접속 휴면 처리
     *  */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* comment.
     *   탈퇴 계정 여부를 표현하는 메소드
     *   false 이면 해당 계정 사용 불가
     *  */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /* comment.
     *   계정 비활성화 여부로 사용자가  사용할 수 없는 상태
     *   false 이면 계정 사용 불가
     *   ex) 삭제 처리 같은 경우
     *  */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
