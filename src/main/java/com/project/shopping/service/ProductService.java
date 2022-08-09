package com.project.shopping.service;


import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    // 상품 생성
    public Product create(Product product){
        if(product == null  && product.getName() == null && product.getTitle()== null
                && product.getContent() == null && product.getPrice() == 0 && product.getTotal() == 0
            && product.getImgUrl() == null){
            throw  new RuntimeException();
        }


        return productRepository.save(product);
    }

    public Product findProductNameUser(int id, User user){
        return productRepository.findByIdAndUserId(id, user);
    }
    public void deleteProduct(Product product){productRepository.delete(product);
    }
    public Product findproductid(int id){
        return productRepository.findById(id);
    }

    public List<Product> findall(){
        return productRepository.findAll();
    }
}
