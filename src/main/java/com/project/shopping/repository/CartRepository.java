package com.project.shopping.repository;

import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart,Integer>,CartRepositoryCustom {
   // 유저가 등록한 상품 전체 조회 , 유저id와 카트 id로 해당 장바구니 상품 찾기 (삭제를 위해서)

    List<Cart> findAllByUser(User user);

    Optional<Cart> findCartByUserAndId(User user, int id);

    boolean existsCartByUserAndProductAndStatus(User user, Product product, String status);

    Optional<Cart> findCartByUserAndProduct(User user, Product product);
    Boolean existsByUserAndId(User user, int id);

    @Override
    List<Cart> getEqUserAndCart(User user, String status);
}
