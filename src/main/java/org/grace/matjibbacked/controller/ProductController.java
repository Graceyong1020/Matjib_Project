package org.grace.matjibbacked.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Product;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.PageResponseDTO;
import org.grace.matjibbacked.dto.ProductDTO;
import org.grace.matjibbacked.dto.TodoDTO;
import org.grace.matjibbacked.service.ProductService;
import org.grace.matjibbacked.util.CustomFileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    // 등록
   /* @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {

        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles(); // 파일 정보 가져오기

        List<String> uploadedFileNames = fileUtil.uploadFiles(files); // 파일 업로드

        productDTO.setUploadFileNames(uploadedFileNames); // 파일명 저장

        log.info(uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }*/

    // file 조회
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    /*@PreAuthorize("hasAnyRole('ROLE_USER, ROLE_ADMIN')") // 사용자, 관리자 권한
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);
        List<ProductDTO> dtoList = responseDTO.getDtoList();

        log.info("Product DTO List: {}", dtoList);

        return responseDTO;
    }*/

    @PreAuthorize("hasAnyRole('ROLE_USER')") // 사용자 권한
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        try { // 테스트를 위해 2초 대기
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        if (uploadedFileNames == null || uploadedFileNames.isEmpty()) {
            // 파일이 업로드되지 않았을 경우 기본 이미지 설정
            uploadedFileNames = List.of("default-thumbnail.jpg");
        }

        productDTO.setUploadFileNames(uploadedFileNames);

        log.info(uploadedFileNames);

        // 서비스 호출
        Long pno = productService.register(productDTO);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("result", pno);

    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name = "pno") Long pno) {

        try { // 테스트를 위해 2초 대기
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);
        ProductDTO oldProductDTO = productService.get(pno); //기존 파일명 가져오기

        // 새로 업로드 해야 하는 파일
        List<MultipartFile> files = productDTO.getFiles();
        //새로 업로드 되어 저장된 파일명
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);
        //기존 파일(DB에 저장된 파일) 수정 과정에서 삭제되었을 수 있음
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        //계속 유지된 파일명
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        // 유지되는 파일 + 새로 업로드된 파일
        if (currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
            uploadedFileNames.addAll(currentUploadFileNames); //기존 파일명에 현재 파일명 추가
        }
        // 수정
        productService.modify(productDTO);

        if (oldFileNames != null && oldFileNames.size() > 0) {
            // 지워야 하는
            // 예전 파일들 중에서 지워져야 하는 파일들
            List<String> removeFiles =
                    oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1)
                            .collect(Collectors.toList());
            // 파일 삭제
            fileUtil.deleteFiles(removeFiles); //기존 파일명이 현재 파일명에 없으면 삭제
        }
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {

        //지워야 하는 파일명 가져오기
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        // 파일 삭제
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);
        return Map.of("RESULT", "SUCCESS");
    }


}


