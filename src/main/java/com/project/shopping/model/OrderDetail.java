package com.project.shopping.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class OrderDetail extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderDetail_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @JoinColumn(name = "orderID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Product_ID")
    private Product product;

    @Column(nullable = false)
    private int productNum;

    @Builder
    public OrderDetail(Product product, int productNum) {
        this.product = product;
        this.productNum = productNum;
    }
}
