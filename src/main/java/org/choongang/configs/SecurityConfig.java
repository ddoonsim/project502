package org.choongang.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.member.service.LoginFailureHandler;
import org.choongang.member.service.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 시큐리티 설정 클래스
 */
@Configuration
@EnableMethodSecurity    // 메서드 단위 별로 인가 설정을 활성화
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /* 인증 설정 S - 로그인, 로그아웃 */
        http.formLogin(f -> {
            f.loginPage("/member/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())    // 로그인 성공 시의 상세한 설정
                    .failureHandler(new LoginFailureHandler()) ;  // 로그인 실패 시의 상세한 설정
        });

        http.logout(c -> {
            c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login") ;    // 로그아웃 시에 이동할 페이지
        });
        /* 인증 설정 E - 로그인, 로그아웃 */

        /* 인가 설정 S - 접근 통제 */
        http.authorizeHttpRequests(c -> {
            c.requestMatchers("/mypage/**").authenticated()    // mypage를 포함한 하위 페이지를 회원 전용 페이지로 설정
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "MANAGER")
                        // admin을 포함한 하위 페이지를 관리자 전용 페이지로
                    .anyRequest().permitAll() ;    // 그 외 모든 페이지는 모든 권한으로 접근 가능
        });

        http.exceptionHandling(c -> {
            // 권한이 허용하지 않은 페이지에 접근한 경우에 띄울 view 페이지를 상세하게 설정
            c.authenticationEntryPoint((req, res, e) -> {
                String URL = req.getRequestURI() ;
                if (URL.indexOf("/admin") != -1) {    // 관리자 페이지 ==> 403 응답 코드
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else {    // 회원 전용 페이지 ==> 로그인 페이지로
                    res.sendRedirect(req.getContextPath() + "/member/login");
                }
            });
        });
        /* 인가 설정 E - 접근 통제 */

        // 시큐리티 설정 무력화
        return http.build() ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }
}
