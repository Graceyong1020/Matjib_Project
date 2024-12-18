package org.grace.matjibbacked.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino;

    private int qty; // overall rating

    private Long pno; // restaurant number

    private String pname; // restaurant name

    private int price; // budget

    private String imageFile;

    // 생성자
    public CartItemListDTO(Long cino, int qty, Long pno, String pname, int price, String imageFile){
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}
