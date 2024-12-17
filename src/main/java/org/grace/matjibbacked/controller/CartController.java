package org.grace.matjibbacked.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.dto.CartItemDTO;
import org.grace.matjibbacked.dto.CartItemListDTO;
import org.grace.matjibbacked.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController { //jwd 인증 받은 사용자만 접근 가능

    private final CartService cartService;

    @PostMapping("/change")
    @PreAuthorize("#itemDTO.email == authentication.name") //로그인한 사용자만 접근 가능
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO itemDTO) {

        log.info(itemDTO);

        if (itemDTO.getQty() <= 0) { //수량이 0이하면 삭제
            return cartService.remove((itemDTO.getCino())); //장바구니에서 삭제
        }
        return cartService.addOrModify(itemDTO); //장바구니에 추가 또는 수정
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items") //장바구니에 담긴 상품 목록 가져오기
    public List<CartItemListDTO> getCartItems(Principal principal) { // 로그인 후 가져오는 기능

        String email = principal.getName();
        log.info("----------------------------------------------");
        log.info("email" + email); //로그인한 사용자의 정보

        return cartService.getCartItems(email); // 이메일로 사용자의 정보 가져오기
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}") //장바구니에서 상품 삭제
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {

        log.info("cart item no: " + cino); //장바구니에서 삭제할 상품 번호
        return cartService.remove(cino);
    }


}
