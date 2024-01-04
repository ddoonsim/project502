package org.choongang.file.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.BaseMember;

import java.util.UUID;

/**
 * 파일 엔터티
 */
@Data @Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_fInfo_gid", columnList = "gid"),
        @Index(name = "idx_fInfo_gid_loc", columnList = "gid, location")
})
public class FileInfo extends BaseMember {
    @Id @GeneratedValue
    private Long seq ;    // 파일 등록 번호, 서버에 업로드하는 파일명의 기준

    @Column(length = 65, nullable = false)
    private String gid = UUID.randomUUID().toString() ;    // 랜덤, unique 그룹아이디

    @Column(length = 65)
    private String location ;    // 파일이 노출되는 위치

    @Column(length = 80)
    private String fileName ;    // 원본 파일명

    @Column(length = 30)
    private String extension ;    // 확장자

    private boolean done ;    // 파일 업로드 완료 여부
}
