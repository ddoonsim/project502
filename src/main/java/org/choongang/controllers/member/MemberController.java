package org.choongang.controllers.member;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final Utils utils ;    // 편의 기능

    /**
     * 회원가입
     */
    @GetMapping("/join")
    public String join() {

        // device가 모바일인지 PC인지에 따라 view 경로 분리
        return utils.tpl("member/join") ;
    }
}
