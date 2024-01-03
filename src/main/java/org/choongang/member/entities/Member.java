package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.choongang.commons.entities.Base;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member extends Base {
    @Id @GeneratedValue
    private Long seq ;

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
}
