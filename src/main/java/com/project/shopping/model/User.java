package com.project.shopping.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String userId;

    @Column(nullable = false)
    private String email;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(nullable = false)
    private String password;

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


    private String roles;


    public List<String> getRoleList(){
        if(this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }

    @Builder
    public User(String email,String password, String username, String address, int age, String nickname, String phoneNumber ,String roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}
