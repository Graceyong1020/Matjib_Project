package org.grace.matjibbacked.repository;

import org.grace.matjibbacked.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart where cart.owner.email = :email")
    // email로 cart를 찾는 쿼리
    Optional<Cart> getCartOfMember(@Param("email")String email);
}
