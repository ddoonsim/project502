package org.choongang.member.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.ListData;
import org.choongang.commons.Pagination;
import org.choongang.commons.Utils;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileInfoService;
import org.choongang.member.controllers.MemberSearch;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.choongang.member.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {    // 회원 조회

    private final MemberRepository memberRepository ;
    private final FileInfoService fileInfoService ;
    private final EntityManager em ;
    private final HttpServletRequest request ;

    /**
     * 회원 조회
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)    // 이메일로 조회
                .orElseGet(() -> memberRepository.findByUserId(username)    // 아이디로 조회
                        .orElseThrow(() -> new UsernameNotFoundException(username))) ;    // 이메일도, 아이디도 없으면 예외 던짐

        List<SimpleGrantedAuthority> authorities = null ;
        List<Authorities> tmp = member.getAuthorities() ;    // Authorities의 데이터 가져오기
        if (tmp != null) {
            // getAuthorities()를 SimpleGrantedAuthority로 변환
            authorities = tmp.stream()
                    .map(s -> new SimpleGrantedAuthority(s.getAuthority().name()))    // SimpleGrantedAuthority의 매개값에는 문자열만 가능하기 때문에 상수 --> String으로 변환
                    .toList() ;
        }

        /* 프로필 이미지 출력 S */
        List<FileInfo> files = fileInfoService.getListDone(member.getGid()) ;    // 업로드 완료된 프로필 이미지만 가져오기
        if (files != null && !files.isEmpty()) {
            member.setProfileImage(files.get(0));    // 프로필 이미지를 멤버 엔터티에 추가
        }
        /* 프로필 이미지 출력 E */

        return MemberInfo.builder()
                .email(member.getEmail())
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }

    /**
     * 회원 목록 반환
     */
    public ListData<Member> getList(MemberSearch search) {

        int page = Utils.onlyPositiveNumber(search.getPage(), 1) ;    // 현재 페이지 번호
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20) ;    // 한 페이지 당 노출할 레코드 개수
        int offset = (page - 1) * limit ;    // 레코드 시작 위치 번호

        BooleanBuilder andBuilder = new BooleanBuilder() ;
        QMember member = QMember.member ;

        PathBuilder<Member> pathBuilder = new PathBuilder<>(Member.class, "member") ;

        List<Member> items = new JPAQueryFactory(em)
                .selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(andBuilder)    // 조건식
                .limit(limit)
                .offset(offset)
                .orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt")))    // 정렬
                .fetch() ;

        /* 페이징 처리 S */
        int total = (int) memberRepository.count(andBuilder) ;    // 총 레코드 개수
        Pagination pagination = new Pagination(page, total, 10, limit, request) ;
        /* 페이징 처리 E */

        return new ListData<>(items, pagination) ;
    }
}
