package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.Utils;
import org.choongang.member.service.FindPwService;
import org.choongang.member.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@SessionAttributes("EmailAuthVerified")
public class MemberController implements ExceptionProcessor {    // 이 컨트롤러에서 발생한 예외는 ExceptionProcessor로 유입되게 하여 공통으로 처리

    private final Utils utils ;
    private final JoinService joinService ;
    private final FindPwService findPwService ;

    /**
     * 회원가입 폼 템플릿으로 연결
     */
    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);

        // 이메일 인증 여부 false로 초기화
        model.addAttribute("EmailAuthVerified", false) ;

        return utils.tpl("member/join");    // 템플릿 주소로 연결
    }

    /**
     * 회원가입 처리 프로세서
     * 회원가입 성공 ==> 로그인 페이지로 이동
     */
    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model, SessionStatus sessionStatus) {
        commonProcess("join", model);

        joinService.process(form, errors);    // 유효성 검사 & 회원가입 DB 처리

        if (errors.hasErrors()) {    // 유효성 검사를 통과하지 못한 경우
            return utils.tpl("member/join");
        }

        // EmailAuthVerified 세션값 비우기
        sessionStatus.setComplete() ;
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

        List<String> addCommonScript = new ArrayList<>() ;    // 공통 자바스크립트
        List<String> addCss = new ArrayList<>() ;
        List<String> addScript = new ArrayList<>() ;    // 프론트 자바스크립트

        if (mode.equals("login")) {
            pageTitle = Utils.getMessage("로그인", "commons") ;
        } else if (mode.equals("join")) {
            // 공통 JS
            addCommonScript.add("fileManager") ;
            // 회원가입 페이지를 위한 CSS, JS
            addCss.add("member/join") ;
            addScript.add("member/join") ;
            addScript.add("member/form") ;

        } else if (mode.equals("find_pw")) {
            // 비밀번호 찾기
            pageTitle = Utils.getMessage("비밀번호_찾기", "commons") ;
        }
        model.addAttribute("pageTitle", pageTitle) ;
        model.addAttribute("addCss", addCss) ;
        model.addAttribute("addScript", addScript) ;
        model.addAttribute("addCommonScript", addCommonScript) ;
    }

    /**
     * 비밀번호 찾기 양식
     */
    @GetMapping("/find_pw")
    public String findPw(@ModelAttribute RequestFindPw form, Model model) {
        commonProcess("find_pw", model);

        return utils.tpl("member/find_pw");
    }

    /**
     * 비밀번호 찾기 처리
     */
    @PostMapping("/find_pw")
    public String findPwPs(@Valid RequestFindPw form, Errors errors, Model model) {
        commonProcess("find_pw", model);

        findPwService.process(form, errors); // 비밀번호 찾기 처리

        if (errors.hasErrors()) {
            return utils.tpl("member/find_pw");
        }

        // 비밀번호 찾기에 이상 없다면 완료 페이지로 이동
        return "redirect:/member/find_pw_done";
    }

    /**
     * 비밀번호 찾기 완료 페이지
     *
     * @param model
     * @return
     */
    @GetMapping("/find_pw_done")
    public String findPwDone(Model model) {
        commonProcess("find_pw", model);

        return utils.tpl("member/find_pw_done");
    }

}
