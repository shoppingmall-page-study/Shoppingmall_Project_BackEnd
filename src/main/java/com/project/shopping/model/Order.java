package com.project.shopping.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="orderID")
    private int id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> products = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User user;

    @Setter
    @Column(nullable = true, length=1000)
    private String orderComplete;

    @Setter
    @Column(nullable = true)
    private long amount;

    @CreatedDate
    private LocalDateTime orderTime;

    @Setter
    @Column
    private LocalDateTime  paymentCompleteDate;

    @Column(nullable = false)
    private String status;

    @PrePersist
    public void createdAt() {
        this.orderTime = LocalDateTime.now();
    }

    @Builder
    public Order(ArrayList<OrderDetail> products, User user, String orderComplete, long amount, LocalDateTime paymentCompleteDate, String status) {
        this.products = products;
        this.user = user;
        this.orderComplete = orderComplete;
        this.amount = amount;
        this.paymentCompleteDate = paymentCompleteDate;
        this.status = status;
    }

    public void addProduct(OrderDetail orderDetail){
        products.add(orderDetail);
        orderDetail.setOrder(this);
    }

}
