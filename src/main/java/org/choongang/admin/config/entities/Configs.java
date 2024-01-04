package org.choongang.admin.config.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

/**
 * 데이터만 필요한 엔터티인 경우, 여러 개의 컬럼을 정의 X
 * JSON 형식의 데이터 컬럼만 정의하여 CRUD 작업 수행
 */
@Data
@Entity
public class Configs {
    @Id
    @Column(length = 60)
    private String code ;
    @Lob
    private String data ;    // JSON 형태의 데이터
}
