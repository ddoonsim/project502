package org.choongang.member.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 로그인 실패 시의 프로세서 구체적인 설정
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();
        // 세션 로그인 실패 메세지 일괄 삭제 처리
        MemberUtil.clearLoginData(session);

        String username = request.getParameter("username") ;
        String password = request.getParameter("password") ;

        session.setAttribute("username", username);    // 입력한 아이디 값을 유지시키기 위해

        // 아이디와 비번은 필수 입력 사항이므로 빈칸 ==> 메세지 출력
        if (!StringUtils.hasText(username)) {
            session.setAttribute("NotBlank_username", Utils.getMessage("NotBlank.userId"));
        }
        if (!StringUtils.hasText(password)) {
            session.setAttribute("NotBlank_password", Utils.getMessage("NotBlank.password"));
        }

        // 아이디와 비번에 모두 값을 입력했으나 로그인 실패한 경우
            // 아이디로 조회되는 회원이 없거나, 비번이 일치하지 X
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            session.setAttribute("Global_error", Utils.getMessage("Fail.login", "errors"));
        }

        // 로그인 페이지로 이동
        response.sendRedirect(request.getContextPath() + "/member/login");
    }
}
