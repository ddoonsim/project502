package org.choongang.member.service;

import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {    // 회원 조회

    private final MemberRepository memberRepository ;

    /**
     * 회원 조회
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)    // 이메일로 조회
                .orElseGet(() -> memberRepository.findByUserId(username)    // 아이디로 조회
                        .orElseThrow(() -> new UsernameNotFoundException(username))) ;    // 이메일도, 아이디도 없으면 예외 던짐


        return MemberInfo.builder()
                .email(member.getEmail())
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .build();
    }
}
