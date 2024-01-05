package org.choongang.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Component;

/**
 * member의 편의 기능
 */
@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final HttpSession session ;

    /**
     * 사용자가 관리자인지 여부
     */
    public boolean isAdmin() {
        if (isLogin()) {
            return getMember().getAuthorities()
                    .stream().map(Authorities::getAuthority)    // Authority 가져오기
                    .anyMatch(a -> a == Authority.ADMIN || a == Authority.MANAGER);    // Authority가 관리자이면 true 반환
        }

        return false ;
    }

    /**
     * 로그인 여부를 확인하는 메서드
     *     ## 회원 정보가 없으면(getMember()가 false이면) ==> 로그인 X
     */
    public boolean isLogin() {
        return getMember() != null ;
    }

    /**
     * 세션에 저장되어 있는 회원 정보 가져오기
     */
    public Member getMember() {
        Member member = (Member) session.getAttribute("member") ;

        return member ;
    }

    /**
     * 로그인 할 때 세션 영역 내의 데이터 삭제
     */
    public static void clearLoginData(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("NotBlank_username");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("Global_error");
    }
}
