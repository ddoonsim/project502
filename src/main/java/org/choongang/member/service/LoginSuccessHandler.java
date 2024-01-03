package org.choongang.member.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 로그인 성공 시의 프로세서 구체적인 설정
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        // 세션에 저장된 메세지 일괄 삭제 처리
        MemberUtil.clearLoginData(session);

        // authentication객체를 통해 회원 정보 조회
        MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal() ;
        Member member = memberInfo.getMember() ;
        session.setAttribute("member", member);    // 세션에 저장

        // redirectUrl(쿼리스트링)에 값이 있으면 해당 주소로 페이지 이동
                                 // 값이 없으면 메인페이지로 이동
        String redirectURL = request.getParameter("redirectURL") ;
        redirectURL = StringUtils.hasText(redirectURL) ? redirectURL : "/" ;
        response.sendRedirect(request.getContextPath() + redirectURL);
    }
}
