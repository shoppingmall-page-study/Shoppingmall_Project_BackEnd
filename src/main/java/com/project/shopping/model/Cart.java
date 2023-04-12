package com.project.shopping.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Cart_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Product_ID")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_ID")
    private User user;

    // set builder 패턴으로 수정
    @Setter
    private long  productNum; // 상품 개수

    @Setter
    @Column(nullable = false)
    private  String status;

    @Builder
    public Cart(Product product, User user, long productNum, String status) {
        this.product = product;
        this.user = user;
        this.productNum = productNum;
        this.status = status;
    }
}
