package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.service.JoinService;
import org.choongang.member.service.MemberInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {    // 이 컨트롤러에서 발생한 예외는 ExceptionProcessor로 유입되게 하여 공통으로 처리

    private final Utils utils ;
    private final JoinService joinService ;

    /**
     * 회원가입 폼 템플릿으로 연결
     */
    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form) {

        return utils.tpl("member/join");    // 템플릿 주소로 연결
    }

    /**
     * 회원가입 처리 프로세서
     * 회원가입 성공 ==> 로그인 페이지로 이동
     */
    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors) {

        joinService.process(form, errors);    // 유효성 검사 & 회원가입 DB 처리

        if (errors.hasErrors()) {    // 유효성 검사를 통과하지 못한 경우
            return utils.tpl("member/join");
        }
        // 유효성 검사 통과 --> 로그인 페이지로
        return "redirect:/member/login" ;
    }

    /**
     * 로그인 페이지로 연결
     */
    @GetMapping("/login")
    public String login() {

        return utils.tpl("member/login");
    }

    /*@ResponseBody    // Rest방식으로 전환
    @GetMapping("/info")
    public void info(Principal principal) {
        String username = principal.getName();    // 아이디만 가져오기
        System.out.printf("username=%s%n", username);
    }*/
    /*@ResponseBody    // Rest방식으로 전환
    @GetMapping("/info")
    public void info(@AuthenticationPrincipal MemberInfo memberInfo) {
        System.out.println(memberInfo);
    }*/
    @ResponseBody
    @GetMapping("/info")
    public void info() {
        // 회원 정보 가져오기
        MemberInfo memberInfo = (MemberInfo) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal() ;
        System.out.println(memberInfo);
    }

}
