package org.grace.matjibbacked.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino;

    private int qyt; // overall rating

    private Long pno; // restaurant number

    private int price; // budget

    private String imageFile;

    // 생성자
    public CartItemListDTO(Long cino, int qyt, Long pno, int price, String imageFile) {
        this.cino = cino;
        this.qyt = qyt;
        this.pno = pno;
        this.price = price;
        this.imageFile = imageFile;
    }
}
