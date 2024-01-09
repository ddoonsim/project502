package org.choongang.file.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.commons.Utils;
import org.choongang.commons.exceptions.UnAuthorizedException;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.entities.QFileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 파일 삭제 기능
 */
@Service
@RequiredArgsConstructor
public class FileDeleteService {

    private final FileInfoService infoService ;
    private final MemberUtil memberUtil ;
    private final FileInfoRepository repository ;

    public void delete(long seq) {
        FileInfo data = infoService.get(seq) ;

        // 파일 삭제 권한 체크
        Member member = memberUtil.getMember() ;
        String createdBy = data.getCreatedBy() ;
        if (StringUtils.hasText(createdBy) && (!memberUtil.isLogin() || (!memberUtil.isAdmin()
                && StringUtils.hasText(createdBy)
                && !createdBy.equals(member.getUserId())))) {    // 현재 로그인된 사용자의 아이디와 파일 업로드한 사용자의 아이디가 같지 않으면
            throw new UnAuthorizedException(Utils.getMessage("Not.your.file", "errors")) ;
        }

        // 원본 파일 삭제
        File file = new File(data.getFilePath()) ;
        if (file.exists()) {
            file.delete() ;
        }

        // 썸네일 파일 삭제
        List<String> thumbsPath = data.getThumbsPath() ;
        if (thumbsPath != null) {
            for (String path : thumbsPath) {
                File thumbFile = new File(path) ;
                if (thumbFile.exists()) thumbFile.delete() ;
            }
        }

        // DB에서 삭제
        repository.delete(data);
        repository.flush();
    }

    /**
     * gid와 location을 가지고 삭제 처리
     */
    public void delete(String gid, String location) {
        QFileInfo fileInfo = QFileInfo.fileInfo ;
        BooleanBuilder builder = new BooleanBuilder() ;
        builder.and(fileInfo.gid.eq(gid)) ;

        if (StringUtils.hasText(location)) {
            builder.and(fileInfo.location.eq(location)) ;
        }

        List<FileInfo> items = (List<FileInfo>) repository.findAll(builder) ;

        items.forEach(i -> delete(i.getSeq()));    // 삭제 처리
    }

    /**
     * location을 지정하지 않았을 경우의 삭제 처리
     */
    public void delete(String gid) {
        delete(gid, null);
    }
}
