package org.choongang.file.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * 파일 다운로드 기능의 원리 이해하기
 *     ## 간략하고 더 용이한 방법이 있음!
 */
@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService ;
    private final HttpServletResponse response ;

    public void download(Long seq) {
        FileInfo data = infoService.get(seq) ;
        String filePath = data.getFilePath() ;

        // 파일명 --> 2바이트 인코딩으로 변경 (윈도우즈 시스템에서 한글 깨짐 방지)
        String fileName = null ;
        try {
            fileName = new String(data.getFileName().getBytes(), "ISO8859_1") ;
        } catch (UnsupportedEncodingException e) {}

        File file = new File(filePath) ;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream out = response.getOutputStream() ;    // 응답 Body에 출력(바이트 단위)

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);    // 출력 방향을 파일로 변경
            response.setHeader("Content-Type", "application/octet-stream");    // Content-Type을 범용적인 타입으로 설정
            response.setIntHeader("Expires", 0);    // 만료 시간 X    ## 파일 용량이 큰 경우 다운로드 중에 서버 만료되지 않게
            response.setHeader("Cache-Control", "must-revalidate");    // 캐시를 사용하더라도 계속 갱신하게 설정    ## 동일한 파일명이라도 다운로드 O
            response.setHeader("Pragma", "public");    // 위 코드와 동일한 기능을 하는 옛날 브라우저에 적용되는 코드
            response.setHeader("Content-Length", String.valueOf(file.length()));    // 파일 용량에 대한 정보

            while (bis.available() > 0) {
                out.write(bis.read());
            }

            out.flush();    // 버퍼 비우기
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
