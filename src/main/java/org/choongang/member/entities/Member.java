package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.choongang.commons.entities.Base;
import org.choongang.file.entities.FileInfo;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member extends Base {
    @Id @GeneratedValue
    private Long seq ;

    @Column(length = 65, nullable = false)
    private String gid ;    // groupId : 파일과 연결하기 위한

    @Column(length = 80, nullable = false, unique = true)
    private String email ;    // NOT NULL, UNIQUE

    @Column(length = 40, nullable = false, unique = true)
    private String userId ;    // NOT NULL, UNIQUE

    @Column(length = 65, nullable = false)
    private String password ;    // NOT NULL

    @Column(length = 40, nullable = false)
    private String name ;    // NOT NULL

    @ToString.Exclude
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)    // 필요할 때 조인
    private List<Authorities> authorities = new ArrayList<>() ;

    @Transient    // 로직 내부에서만 사용할 컬럼
    private FileInfo profileImage ;    // 프로필 이미지 파일의 정보
}
