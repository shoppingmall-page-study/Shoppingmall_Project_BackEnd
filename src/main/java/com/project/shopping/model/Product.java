package com.project.shopping.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Product_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User userId;


    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Lob
    private String content;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private long amount;

    @Setter
    @Column(nullable = false)
    private int total;

    @Setter
    @Column(nullable = false)
    private String imgUrl;

    @Setter
    @CreationTimestamp
    private Timestamp createDate;


    @Setter
    @CreationTimestamp
    private Timestamp modifiedDate;



    @OneToMany(mappedBy = "productId")
    private List<Review> reviews = new ArrayList<>(); // 일대 다 review와 연관 관계 맺기


    @OneToMany(mappedBy = "productId")
    private  List<Cart> carts = new ArrayList<>();


    @Setter
    @Column(nullable = false)
    private  String status;


    @Builder
    public Product(User userId, String title, String content, String name, long amount, int total, String imgUrl, Timestamp createDate,Timestamp modifiedDate, String status) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.amount = amount;
        this.total = total;
        this.imgUrl = imgUrl;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
    }
}
