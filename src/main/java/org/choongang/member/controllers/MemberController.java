package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {    // 이 컨트롤러에서 발생한 예외는 ExceptionProcessor로 유입되게 하여 공통으로 처리
    private final Utils utils ;

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
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model) {

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
}
