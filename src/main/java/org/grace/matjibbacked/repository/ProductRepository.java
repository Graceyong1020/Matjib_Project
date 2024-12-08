package org.grace.matjibbacked.repository;

import org.grace.matjibbacked.domain.Product;
import org.grace.matjibbacked.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductSearch {

    @EntityGraph(attributePaths = "imageList") // imageList를 같이 가져오기 위해
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    @Modifying // update, delete 쿼리를 실행하기 위한 어노테이션
    @Query("update Product p set p.delFlag = :delFlag where p.pno = :pno")
    void updateToDelete( @Param("pno") Long pno, @Param("delFlag") boolean flag);

    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> selectList(Pageable pageable);
    }

