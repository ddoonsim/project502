package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.rests.JSONData;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.service.FileUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController    // 응답바디가 JSON형식으로
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class ApiFileController implements ExceptionRestProcessor {

    private final FileUploadService uploadService ;

    @PostMapping
    public JSONData<List<FileInfo>> upload(@RequestParam("file") MultipartFile[] files,
                           @RequestParam(name = "gid", required = false)String gid,
                           @RequestParam(name = "location", required = false)String location,
                           @RequestParam(name = "imageOnly", required = false)boolean imageOnly) {

        List<FileInfo> uploadedFiles =  uploadService.upload(files, gid, location, imageOnly) ;

        return new JSONData<>(uploadedFiles) ;    // JSON 형식으로 출력
    }
}
