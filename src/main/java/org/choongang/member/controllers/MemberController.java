package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
    private final JoinService joinService ;

    /**
     * 회원가입 폼 템플릿으로 연결
     */
    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);

        return utils.tpl("member/join");    // 템플릿 주소로 연결
    }

    /**
     * 회원가입 처리 프로세서
     * 회원가입 성공 ==> 로그인 페이지로 이동
     */
    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model) {
        commonProcess("join", model);

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
    public String login(Model model) {
        commonProcess("login", model);

        return utils.tpl("member/login");
    }

    /**
     * 컨트롤러에 공통으로 처리할 내용
     */
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "join" ;
        String pageTitle = Utils.getMessage("회원가입", "commons") ;
        if (mode.equals("login")) {
            pageTitle = Utils.getMessage("로그인", "commons") ;
        }
        model.addAttribute("pageTitle", pageTitle) ;
    }

}
