package org.grace.matjibbacked.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Cart;
import org.grace.matjibbacked.domain.CartItem;
import org.grace.matjibbacked.domain.Member;
import org.grace.matjibbacked.domain.Product;
import org.grace.matjibbacked.dto.CartItemDTO;
import org.grace.matjibbacked.dto.CartItemListDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void testInsertByProduct() {

        // 사용자 전송 정보
        String email = "user1@aaa.com";
        Long pno = 4L;
        int qty = 4;

        // 이메일 상품번호로 장바구니 아이템 확인. 있으면 수량만 변경, 없으면 추가
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        //이미 사용자의 장바구니에 담겨있는 상품
        if (cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);

            return;
        }
        // 사용자 장바구니에 장바구니 아이템 만들어서 저장
        Optional<Cart> result = cartRepository.getCartOfMember(email);
        Cart cart = null;
        //장바구니 자체가 없음
        if (result.isEmpty()) {
            log.info("MemeberCart is not exist");
            //카트 만들어줘야 한다
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        } else { //장바구니가 있지만 아이템이 없음
            cart = result.get();
        }
        log.info(cart);

        //if (cartItem == null) {
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        //}
        //장바구니에 상품 추가
        cartItemRepository.save(cartItem);


    }

    @Test
    public void testListOfMember() {

        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }


    }
    @Transactional
    @Commit
    @Test
    public void testUpdateByCino() { //장바구니 아이템의 수량 변경

        Long cino = 1L;
        int qty = 4;

        Optional<CartItem> result = cartItemRepository.findById(cino);
        CartItem cartItem = result.orElseThrow();
        cartItem.changeQty(qty);
        cartItemRepository.save(cartItem);
    }

    @Test
    public void testDeleteThenList() {
        Long cino = 4L;
        Long cno = cartItemRepository.getCartFromItem(cino); //장바구니 번호 찾기
        cartItemRepository.deleteById(cino); //장바구니 아이템 삭제
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOBYCart(cno); //장바구니 번호로 장바구니 아이템 리스트 가져오기

        for(CartItemListDTO dto : cartItemList) { //장바구니 아이템 리스트 출력
            log.info(dto);
        }
    }
}
