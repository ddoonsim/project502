package org.choongang;

import org.choongang.member.Authority;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectApplicationTests {

	@Autowired
	private MemberRepository memberRepository ;
	@Autowired
	private AuthoritiesRepository authoritiesRepository ;

	@Test @Disabled  // 실행 X
	void contextLoads() {
		// 회원 조회
		Member member = memberRepository.findByUserId("user01").orElse(null) ;

		// ADMIN 권한 부여
		Authorities authorities = new Authorities() ;
		authorities.setMember(member);
		authorities.setAuthority(Authority.ADMIN);

		authoritiesRepository.saveAndFlush(authorities) ;    // UPDATE 쿼리 실행
	}

}
