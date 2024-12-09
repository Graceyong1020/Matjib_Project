package org.grace.matjibbacked.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil { // 파일 업로드, 다운로드, 삭제를 위한 클래스

    @Value("${org.grace.upload.path}")
    private String uploadPath;

    @PostConstruct // 초기화 메서드
    public void init() { // 폴더 만들어주는 메서드
        uploadPath = sanitizePath(uploadPath); // Sanitize the upload path
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("----------------------");
        log.info("uploadPath: " + uploadPath);
    }

    // sanitizePath 메서드: 파일명에 특수문자가 포함되어 있을 경우 _로 치환
    private String sanitizePath(String path) {
        return path.replaceAll("[<>:\"/\\\\|?*]", "_");
    }

    // uploadFiles 메서드
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if (files == null || files.size() == 0) {
            return null;
        }

        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename().trim();
            String sanitizedFileName = sanitizePath(originalFileName); // 파일명에 특수문자가 포함되어 있을 경우 _로 치환
            String customFileName = "upload" + sanitizedFileName; // Set a custom file name
            String savedName = UUID.randomUUID().toString() + "_" + customFileName; // uuid로 파일명 변경

            Path savePath = Paths.get(uploadPath, savedName);

            try {
                Files.copy(file.getInputStream(), savePath); // 원본 파일 업로드

                String contentType = file.getContentType(); // 파일 타입

                if (contentType != null && contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName); // 썸네일 파일명
                    Thumbnails.of(savePath.toFile()).size(400, 400).toFile(thumbnailPath.toFile()); // 썸네일 생성
                }

                uploadNames.add(savedName); // uuid로 파일명 변경 후 저장
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return uploadNames;
    }

    // getFile 메서드
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + sanitizePath(fileName));

        if (!resource.isReadable()) {
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    // deleteFiles 메서드
    public void deleteFiles(List<String> fileNames) {
        if (fileNames == null || fileNames.size() == 0) {
            return;
        }
        fileNames.forEach(fileName -> {
            //Thumbnail 파일 삭제
            String thumbnailFileName = "s_" + sanitizePath(fileName.trim());
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);

            Path filePath = Paths.get(uploadPath, sanitizePath(fileName.trim()));

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}