package com.project.shopping.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false,length = 20)
    private String nickname;

    @Column(nullable = false,length=11)
    private String phoneNumber;

    @Builder
    public User(String email, String username, String address, int age, String nickname, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.address = address;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
