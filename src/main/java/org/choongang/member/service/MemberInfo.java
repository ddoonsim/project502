package org.choongang.member.service;

import lombok.Builder;
import lombok.Data;
import org.choongang.member.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Data
@Builder
public class MemberInfo implements UserDetails {    // 스프링 시큐리티에서 회원정보를 얻을 수 있는 클래스(DTO와 유사)

    private String email ;
    private String userId ;
    private String password ;
    private Member member ;

    private Collection<? extends GrantedAuthority> authorities ;    // 권한

    @Override
    // 인가를 처리하는 메서드
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {    // 비밀번호
        return password;
    }

    @Override
    public String getUsername() {    // 아이디
        return StringUtils.hasText(email) ? email : userId ;    // 이메일과 아이디 모두 혼용 가능하게
    }

    @Override
    // 계정의 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // 계정의 잠금 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 비번의 만료 여부 --> 비번을 주기적으로 바꿔야하게끔 설정
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // false ==> 회원 탈퇴 처리 후, 로그인이 안 되게 처리
    public boolean isEnabled() {
        return true;
    }
}
