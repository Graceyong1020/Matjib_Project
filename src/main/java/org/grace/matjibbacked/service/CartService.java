package org.grace.matjibbacked.service;

import org.grace.matjibbacked.dto.CartItemDTO;
import org.grace.matjibbacked.dto.CartItemListDTO;

import java.util.List;

public interface CartService {
    // 장바구니에 상품을 추가하거나 수정하는 메소드
    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    // 모든 장바구니 아이템 목록
    List<CartItemListDTO> getCartItems(String email);

    // 장바구니 아이템 삭제
    List<CartItemListDTO> remove(Long cino);

}
