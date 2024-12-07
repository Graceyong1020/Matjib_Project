package org.grace.matjibbacked.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO { // 식당 조회, 등록

    private Long pno; // 상품번호 -> 식당 번호
    private String pname; // 상품명 -> 식당명
    private int price; // 가격 -> 비용
    private String pdesc; // 상품설명 -> 식당설명
    private boolean delFlag; // 삭제여부

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();
    // 조회
    @Builder.Default
    private List<String> uploadedFileNames = new ArrayList<>();


}
