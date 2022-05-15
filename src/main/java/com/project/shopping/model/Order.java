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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "id")
    private User userId;

    @Column(nullable = false)
    private int productNum;

    @Column(nullable = false)
    private boolean orderComplete;

    @CreationTimestamp
    private Timestamp orderTime;
}
