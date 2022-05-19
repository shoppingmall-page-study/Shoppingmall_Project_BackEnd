package com.project.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="User_ID")
    private int id;

    @Column(nullable = false, length = 255)
    private String username;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false,length = 20)
    private String nickname;

    @Column(nullable = false,length=11)
    private String phoneNumber;
}
