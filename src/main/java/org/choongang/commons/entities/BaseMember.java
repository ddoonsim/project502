package org.choongang.commons.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
// 사용자 정보를 공통화
public abstract class BaseMember extends Base {

    @CreatedBy
    @Column(length = 40, updatable = false)
    private String createdBy ;    // 처음 생성한 사람

    @LastModifiedBy
    @Column(length = 40, insertable = false)
    private String modifiedBy ;    // 수정한 사람
}
