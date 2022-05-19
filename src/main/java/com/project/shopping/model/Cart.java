package com.project.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
