package org.choongang.commons.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 공통으로 사용할 컬럼을 담은 추상 클래스
 * 생성날짜, 수정날짜
 */
@Getter @Setter
@MappedSuperclass    // 공통 속성화를 위한 상위 클래스임을 알려주는 어노테이션
@EntityListeners(AuditingEntityListener.class)    // 날짜와 시간 감지를 위해 이벤트 리스너 필요
public abstract class Base {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt ;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt ;
}
