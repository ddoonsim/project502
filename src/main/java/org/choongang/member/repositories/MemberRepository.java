package org.choongang.member.repositories;

import com.querydsl.core.BooleanBuilder;
import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * DAO를 대체 가능
 */
public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
    @EntityGraph(attributePaths = "authorities")    // 선별적으로 즉시 로딩 설정
    Optional<Member> findByEmail(String email) ;
    @EntityGraph(attributePaths = "authorities")    // 선별적으로 즉시 로딩 설정
    Optional<Member> findByUserId(String userId) ;

    /**
     * 이메일이 엔티티 안에 저장되어 있는가
     */
    default boolean existsByEmail(String email) {
        QMember member = QMember.member ;

        return exists(member.email.eq(email)) ;
    }

    /**
     * 아이디가 엔티티 안에 저장되어 있는가
     */
    default boolean existsByUserId(String userId) {
        QMember member = QMember.member ;

        return exists(member.userId.eq(userId)) ;
    }

    /**
     * 이메일과 회원명으로 조회되는지 체크
     */
    default boolean existsByEmailAndName(String email, String name) {
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(member.email.eq(email))
                .and(member.name.eq(name));

        return exists(builder);
    }
}
