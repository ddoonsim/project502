package org.choongang.member.repositories;

import org.choongang.member.entities.Member;
import org.choongang.member.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * DAO를 대체 가능
 */
public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
    Optional<Member> findByEmail(String email) ;
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
}
