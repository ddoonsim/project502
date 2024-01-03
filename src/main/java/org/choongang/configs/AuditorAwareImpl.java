package org.choongang.configs;

import org.choongang.member.service.MemberInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 사용자 정보를 자동으로 저장
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        String userId = null ;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        /* getPrincipal()
         *  로그인 상태 : UserDetails의 구현 객체 : MemberInfo
         *  미로그인 상태 : String 타입의 "anonymousUser" 문자열 반환
         */
        if (auth != null && auth.getPrincipal() instanceof MemberInfo) {
            MemberInfo memberInfo = (MemberInfo) auth.getPrincipal() ;
            userId = memberInfo.getUserId();
        }

        return Optional.ofNullable(userId) ;
    }
}
