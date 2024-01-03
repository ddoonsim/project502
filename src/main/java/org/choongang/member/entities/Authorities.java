package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.choongang.member.Authority;

/**
 * 부여하는 권한의 엔티티
 */
@Data
@Entity
@Table(indexes = @Index(name = "uq_member_authority", columnList = "member_seq, authority", unique = true))
    // 두 컬럼을 결합하여 UNIQUE 제약조건을 추가
public class Authorities {
    @Id @GeneratedValue
    private Long seq ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member ;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Authority authority ;
}
