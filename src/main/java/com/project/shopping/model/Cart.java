package com.project.shopping.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Cart_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User UserId;

    @CreationTimestamp
    private Timestamp createTime;

    @Builder
    public Cart(Product productId, User userId, Timestamp createTime) {
        this.productId = productId;
        UserId = userId;
        this.createTime = createTime;
    }
}
