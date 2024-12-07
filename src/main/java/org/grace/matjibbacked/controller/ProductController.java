package org.grace.matjibbacked.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.dto.ProductDTO;
import org.grace.matjibbacked.util.CustomFileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    // 등록
    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {

        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles(); // 파일 정보 가져오기

        List<String> uploadedFileNames = fileUtil.uploadFiles(files); // 파일 업로드

        productDTO.setUploadedFileNames(uploadedFileNames); // 파일명 저장

        log.info(uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }

    // file 조회
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

}
