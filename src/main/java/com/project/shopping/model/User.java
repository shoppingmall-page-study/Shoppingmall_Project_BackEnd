package com.project.shopping.model;

import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.security.Role;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name ="User_ID")
    private String userId;



    @Column(nullable = false)
    private String email;


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


    @Column(nullable = false,length=13)
    private String phoneNumber;


    @Column(nullable = false)
    private String postCode;

    @Setter
    private Role roles;
    @Column(nullable = false)

    private  String status;


    @Column(nullable = false)
    private boolean passwordEnable;


    public List<Role> getRoleList(){
        if(this.roles!=null){
            return Arrays.asList(this.roles);
        }

        return new ArrayList<>();
    }



    @Builder
    private User(String email,String password, String username, String address, int age, String nickname, String phoneNumber,String postCode ,Role roles, String status) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.roles = roles;
        this.status = status;
        this.passwordEnable = true;
    }


    public void update(final User user){
        updateUserName(user.getUsername());
        updateAddress(user.getAddress());
        updateAge(user.getAge());
        updateNickName(user.getNickname());
        updatePhoneNumber(user.getPhoneNumber());
        updatePostCode(user.getPostCode());
    }

    public void oauthInfoAdd(final User user){
        updateAddress(user.getAddress());
        updatePostCode(user.getPostCode());
        updateAge(user.getAge());
        updateNickName(user.getNickname());
        updatePhoneNumber(user.getPhoneNumber());
        changeRole();
    }
    public void changeRole(){
        this.roles = Role.ROLE_USER;
    }

    public void delete(){
        this.status = "Disable";
    }

    private void updatePostCode(String postCode) {
        this.postCode = postCode;

    }


    private void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    private void updateAge(int age) {
        this.age = age;
    }

    private void updateAddress(String address) {
        this.address = address;
    }

    private void updateUserName(String username) {
        this.username = username;
    }



    // dto 변환

    public UserJoinResponseDTO toUserJoinResponseDTO(){
        return UserJoinResponseDTO.builder()
                .email(this.email)
                .username(this.username)
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

    public UserOAuthAddInfoResponseDTO toUserOAuthAddInfoResponseDTO(){
        return UserOAuthAddInfoResponseDTO.builder()
                .email(this.email)
                .username(this.username)
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

    public UserInfoResponseDTO toUserInfoResponseDTO(){
        return UserInfoResponseDTO.builder()
                .email(this.email)
                .username(this.username)
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

    public UserDeleteResponseDTO toUserDeleteResponseDTO(){
        return UserDeleteResponseDTO.builder()
                .email(this.email)
                .username(this.username)
                .nickname(this.nickname)
                .build();
    }

    public UserUpdateResponseDTO toUserUpdateResponseDTO(){
        return UserUpdateResponseDTO.builder().email(this.email)
                .username(this.username)
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }
}
