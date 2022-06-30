package com.project.shopping.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private String imgUrl;

    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public Product(User userId, String title, String content, String name, long price, int total, String imgUrl, Timestamp createDate) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.price = price;
        this.total = total;
        this.imgUrl = imgUrl;
        this.createDate = createDate;
    }
}
