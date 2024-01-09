package org.choongang.member.service;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {    // 회원 조회

    private final MemberRepository memberRepository ;
    private final FileInfoService fileInfoService ;

    /**
     * 회원 조회
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)    // 이메일로 조회
                .orElseGet(() -> memberRepository.findByUserId(username)    // 아이디로 조회
                        .orElseThrow(() -> new UsernameNotFoundException(username))) ;    // 이메일도, 아이디도 없으면 예외 던짐

        List<SimpleGrantedAuthority> authorities = null ;
        List<Authorities> tmp = member.getAuthorities() ;    // Authorities의 데이터 가져오기
        if (tmp != null) {
            // getAuthorities()를 SimpleGrantedAuthority로 변환
            authorities = tmp.stream()
                    .map(s -> new SimpleGrantedAuthority(s.getAuthority().name()))    // SimpleGrantedAuthority의 매개값에는 문자열만 가능하기 때문에 상수 --> String으로 변환
                    .toList() ;
        }

        /* 프로필 이미지 출력 S */
        List<FileInfo> files = fileInfoService.getListDone(member.getGid()) ;    // 업로드 완료된 프로필 이미지만 가져오기
        if (files != null && !files.isEmpty()) {
            member.setProfileImage(files.get(0));    // 프로필 이미지를 멤버 엔터티에 추가
        }
        /* 프로필 이미지 출력 E */

        return MemberInfo.builder()
                .email(member.getEmail())
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
