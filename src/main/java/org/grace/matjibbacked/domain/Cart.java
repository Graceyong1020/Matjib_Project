package org.grace.matjibbacked.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "table_cart",
    indexes = { @Index(name="idx_cart_email", columnList = "member_owner")})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne // user email 사용 -> user email로 cart를 찾을 수 있음
    @JoinColumn(name="member_owner")
    private Member owner;

}
