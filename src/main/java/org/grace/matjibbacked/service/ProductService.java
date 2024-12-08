package org.grace.matjibbacked.service;


import jakarta.transaction.Transactional;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.PageResponseDTO;
import org.grace.matjibbacked.dto.ProductDTO;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    //등록
    Long register(ProductDTO productDTO);
    //조회
    ProductDTO get(Long pno);

    //수정
    void modify(ProductDTO productDTO);

    void remove(Long pno);
}
