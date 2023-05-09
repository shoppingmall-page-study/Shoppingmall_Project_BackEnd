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

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "Order_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_ID")
    private Product product;

    @Column(nullable = false)
    private int productNum;

    public OrderDetail(Product product, int productNum, Order order) {
        this.order = order;
        this.product = product;
        this.productNum = productNum;
    }


}
