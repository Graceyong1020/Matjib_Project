package org.grace.matjibbacked.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Cart;
import org.grace.matjibbacked.domain.CartItem;
import org.grace.matjibbacked.domain.Member;
import org.grace.matjibbacked.domain.Product;
import org.grace.matjibbacked.dto.CartItemDTO;
import org.grace.matjibbacked.dto.CartItemListDTO;
import org.grace.matjibbacked.repository.CartItemRepository;
import org.grace.matjibbacked.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {

        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        Long cino = cartItemDTO.getCino();

        log.info("=======================================");
        log.info(cartItemDTO.getCino()==null);

        if(cino != null) { //장바구니 아이템이 있음. 수량만 변경
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return getCartItems(email);
        }

        //장바구니 아이템이 없음. 추가
        Cart cart = getCart(email); //사용자의 장바구니를 찾는다
        CartItem cartItem = null;

        cartItem = cartItemRepository.getItemOfPno(email, pno); //사용자의 장바구니에 해당 상품이 있는지 확인

        if(cartItem == null) {
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().cart(cart).product(product).qty(qty).build();
        } else {
            cartItem.changeQty(qty);
        }
        cartItemRepository.save(cartItem);

        return getCartItems(email);

    }

    private Cart getCart(String email) {
        // 해당 사용자의 장바구니가 있는지 확인. 없으면 만들어준다
        Cart cart = null;

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        if (result.isEmpty()) {
            log.info("Cart of the member is not exist");

            Member member = Member.builder().email(email).build();

            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }
        return cart;

    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {

        Long cno = cartItemRepository.getCartFromItem(cino); //cino로 cart번호 찾아서 목록 가져오기

        cartItemRepository.deleteById(cino);

        return cartItemRepository.getItemsOfCartDTOBYCart(cno); // cart번호로 목록 가져오기
    }
}
