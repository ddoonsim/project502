package org.choongang.member.controllers;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 커맨드 객체 : 유효성 검증 용도
 */
@Data
public class RequestJoin {
    @NotBlank @Email
    private String email ;
    @NotBlank @Size(min = 6)
    private String userId ;
    @NotBlank @Size(min = 8)
    private String password ;
    @NotBlank
    private String confirmPassword ;
    @NotBlank
    private String name ;
    @AssertTrue
    private boolean agree ;
}
