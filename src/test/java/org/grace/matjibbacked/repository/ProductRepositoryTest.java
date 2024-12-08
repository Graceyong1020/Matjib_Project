package org.grace.matjibbacked.repository;


import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Product;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert() {

        for(int i = 0; i < 10; i++) {

            Product product = Product.builder()
                    .pname("test")
                    .price(10000)
                    .build();

            product.addImageString(UUID.randomUUID() + "_" + "IMG1");
            product.addImageString(UUID.randomUUID() + "_" + "IMG2");

            productRepository.save(product);
        }
    }

    @Test
    public void testRead() {

        Long pno = 1L;

        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);
        log.info(product.getImageList());

    }
    @Commit
    @Transactional
    @Test
    public void testDelete() {

        Long pno = 1L;

        productRepository.updateToDelete(1L, true);
    }

    @Test
    public void testSearch() {

    PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

    productRepository.searchList(pageRequestDTO);
    }
}
