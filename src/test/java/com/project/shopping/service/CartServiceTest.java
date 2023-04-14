package com.project.shopping.service;

import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.CartRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CartServiceTest {
//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private  ProductService productService;
//    @Autowired
//    private  UserService userService;
//    @Autowired
//    private PasswordEncoder passwordEncoder;



//    @Test
//    public void createCart(){
//        String passworddd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("cart1@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passworddd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .postCode("postcode")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        User createUser = userService.create(user);
//        Product product = Product.builder()
//                .user(createUser)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product createProduct = productService.create(product);
//        Cart cart = Cart.builder().user(createUser).product(createProduct).createTime(Timestamp.valueOf(LocalDateTime.now())).status("active").build();
//        Cart  creatCart = cartService.create(cart);
//
//        Assertions.assertThat(cart.getId()).isEqualTo(creatCart.getId());
//
//
//
//
//    }
//    @Test
//    public  void findCartUserAndId(){
//        String passworddd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("cart2@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passworddd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .postCode("test")
//                .build();
//        User createUser = userService.create(user);
//        Product product = Product.builder()
//                .user(createUser)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .status("active")
//                .imgUrl("asdfasdf")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product createProduct = productService.create(product);
//        Cart cart = Cart.builder().user(createUser).product(createProduct).createTime(Timestamp.valueOf(LocalDateTime.now())).status("active").build();
//        Cart  creatCart = cartService.create(cart); //카트 생성
//
//        Cart findcart = cartService.findCartUserAndId(user, cart.getId());
//        Assertions.assertThat(findcart.getId()).isEqualTo(creatCart.getId());
//
//    }
//
//    @Test
//    public void findallByuser(){
//        String passworddd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("cart3@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passworddd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .postCode("test")
//                .build();
//        User createUser = userService.create(user);
//        Product product = Product.builder()
//                .user(createUser)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product createProduct = productService.create(product);
//        Cart cart = Cart.builder().user(createUser).product(createProduct).createTime(Timestamp.valueOf(LocalDateTime.now())).status("active").build();
//        Cart  creatCart = cartService.create(cart); //카트 생성
//
//        List<Cart> cartList = cartService.findallByuser(createUser);
//        for(Cart carts : cartList){
//            if(carts.getuser().getuser() == user.getuser()){
//                Assertions.assertThat(cart.getuser().getEmail()).isEqualTo(user.getEmail());
//            }
//        }
//
//    }
//
//    @Test
//    public void delete(){
//        String passworddd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("cart4@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passworddd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .postCode("test")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        User createUser = userService.create(user);
//        Product product = Product.builder()
//                .user(createUser)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product createProduct = productService.create(product);
//        Cart cart = Cart.builder().user(createUser).product(createProduct).createTime(Timestamp.valueOf(LocalDateTime.now())).status("active").build();
//        Cart  creatCart = cartService.create(cart); //카트 생성
//
//        Cart findcart = cartService.findCartUserAndId(createUser,creatCart.getId());
//        cartService.deleteCart(findcart);
//
//
//
//
//        Optional<Cart> deleteProduct = Optional.ofNullable(cartService.findCartUserAndId(createUser, creatCart.getId()));
//        org.junit.jupiter.api.Assertions.assertFalse(deleteProduct.isPresent());
//    }
}
