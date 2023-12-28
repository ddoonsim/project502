package org.choongang.controllers.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    /**
     * 회원가입
     */
    @GetMapping("/join")
    public String join() {
        return "front/member/join" ;
    }
}
