package org.choongang.file.service;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.choongang.commons.Utils;
import org.choongang.configs.FileProperties;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 업로드 기능
 */
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)    // 파일 업로드 경로 가져오기
public class FileUploadService {

    private final FileProperties fileProperties ;    // 파일 설정
    private final FileInfoRepository repository ;
    private final FileInfoService infoService ;
    private final Utils utils ;

    public List<FileInfo> upload(MultipartFile[] files, String gid, String location, boolean imageOnly) {
        /**
         * 1. 파일 정보 저장
         * 2. 서버에 파일 업로드 처리
         */

        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString() ;
        String uploadPath = fileProperties.getPath();    // 파일 업로드 기본 경로
        String thumbPath = uploadPath + "thumbs/" ;    // 썸네일 업로드 기본 경로

        List<int[]> thumbsSize = utils.getThumbSize() ;    // 썸네일 사이즈 가져오기

        List<FileInfo> uploadedFiles = new ArrayList<>() ;    // 업로드 성공한 파일 정보

        for (MultipartFile file : files) {
            /* 파일 정보 저장 S */
            String fileName = file.getOriginalFilename();    // 업로드 시 원본 파일명
            String extension = fileName.substring(fileName.lastIndexOf(".")) ;    // 파일의 확장자 명
            String fileType = file.getContentType() ;    // 파일 유형  ex) image
            // 이미지만 업로드 하는 경우, 이미지가 아닌 형식은 업로드에서 배제
            if (imageOnly && fileType.indexOf("image/") == -1) {
                continue;
            }

            FileInfo fileInfo = FileInfo.builder()
                    .gid(gid)
                    .location(location)
                    .fileName(fileName)
                    .extension(extension)
                    .fileType(fileType).build() ;

            repository.saveAndFlush(fileInfo) ;
            /* 파일 정보 저장 E */

            /* 파일 업로드 처리 S */
            long seq = fileInfo.getSeq();
            File dir = new File(uploadPath + (seq % 10)) ;    // 분산 저장을 위한 디렉토리 10개
            if (!dir.exists()) {    // 디렉토리가 없으면 ==> 생성
                dir.mkdir() ;
            }

            File uploadFile = new File(dir, seq + extension) ;
            try {
                file.transferTo(uploadFile);    // 파일 업로드

                /* 썸네일 이미지 처리 S */
                if (fileType.indexOf("image/") != -1 && thumbsSize != null) {    // 이미지파일이면

                    File thumbDir = new File(thumbPath + (seq % 10) + "/" + seq) ;
                    if (!thumbDir.exists()) {
                        thumbDir.mkdirs() ;    // 없는 디렉토리인 경우 하위 경로 폴더 모두 생성
                    }
                    for (int[] sizes : thumbsSize) {
                        String thumbFileName = sizes[0] + "_" + sizes[1] + "_" + seq + extension ;    // 썸네일 파일명 : 너비_높이_파일명

                        File thumb = new File(thumbDir, thumbFileName) ;

                        Thumbnails.of(uploadFile)    // of(원본 파일 명)
                                .size(sizes[0], sizes[1])    // size(가로, 세로)
                                .toFile(thumb);    // toFile(썸네일 이미지 파일)
                    }
                }
                /* 썸네일 이미지 처리 E */

                infoService.addFileInfo(fileInfo);    // 파일 추가 정보 주입
                uploadedFiles.add(fileInfo) ;    // 업로드 성공 ==> 파일 정보 추가
            }
            catch (IOException e) {
                e.printStackTrace();
                // 업로드 실패 ==> 파일 정보 제거
                repository.delete(fileInfo);
                repository.flush();
            }
            /* 파일 업로드 처리 E */
        }

        return uploadedFiles ;
    }

    /**
     * gid 별로 업로드 완료 일괄 처리
     */
    public void processDone(String gid) {
        List<FileInfo> files = repository.findByGid(gid) ;
        if (files == null) {
            return;
        }

        files.forEach(file -> file.setDone(true));
        repository.flush();
    }
}
