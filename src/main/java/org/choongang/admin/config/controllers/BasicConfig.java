package org.choongang.admin.config.controllers;

import lombok.Data;

/**
 * 기본 설정을 위한 커맨드 객체
 */
@Data
public class BasicConfig {
    private String siteTitle = "" ;
    private String siteDescription = "" ;
    private String siteKeywords = "" ;
    private int cssJsVersion = 1 ;
    private String thumbSize = "" ;
    private String joinTerms = "" ;
}
