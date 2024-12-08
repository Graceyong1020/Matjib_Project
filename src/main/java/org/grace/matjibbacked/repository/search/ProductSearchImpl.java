package org.grace.matjibbacked.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Product;
import org.grace.matjibbacked.domain.QProduct;
import org.grace.matjibbacked.domain.QProductImage;
import org.grace.matjibbacked.dto.PageRequestDTO;
import org.grace.matjibbacked.dto.PageResponseDTO;
import org.grace.matjibbacked.dto.ProductDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;


@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {

        log.info("searchList............");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);

        query.leftJoin(product.imageList, productImage); // imageList를 같이 가져오기 위해

        query.where(productImage.ord.eq(0)); // 대표 이미지만 가져오기

        Objects.requireNonNull( getQuerydsl()).applyPagination(pageable, query);
               // 페이징 처리

        List<Tuple> productList = query.select(product, productImage).fetch();

        long count = query.fetchCount(); // 전체 개수

        log.info("=======================================");
        log.info("productList: " + productList);

        return null;
    }
}
