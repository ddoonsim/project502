package org.choongang.member.service;

import lombok.RequiredArgsConstructor;
import org.choongang.member.controllers.JoinValidator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository ;
    private final JoinValidator validator ;
    private final PasswordEncoder encoder ;

    public void process(RequestJoin form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) {    // 검증 실패 시, 메서드 종료
            return;
        }

        // 검증 성공 시, 회원가입 처리
        // 비번 BCrypt로 해시화
        String hash = encoder.encode(form.getPassword()) ;

//        Member member = new ModelMapper().map(form, Member.class);    // 자동 setter
        Member member = new Member() ;
        member.setEmail(form.getEmail());
        member.setUserId(form.getUserId());
        member.setPassword(hash);
        member.setName(form.getName());

        process(member);    // DB에 신규회원 데이터 추가하는 DB 처리
    }

    /**
     * DB 처리
     */
    public void process(Member member) {
        memberRepository.saveAndFlush(member) ;
    }
}
