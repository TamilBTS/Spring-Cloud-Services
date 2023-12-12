package com.spring.product.serv;

import com.spring.product.entity.Product;
import com.spring.product.repo.ProductRepo;
import com.spring.product.vo.ProductResponse;
import com.spring.product.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServ {

    @Autowired
    ProductRepo productRepo;
    public void save(ProductVo productVo){
        Product product = Product.builder()
                .name(productVo.getName())
                .description(productVo.getDescription())
                .price(productVo.getPrice())
                .build();
        productRepo.save(product);
    }

    public Product findById(String id){
        return productRepo.findById(id).get();
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepo.findAll();
        return products.stream().map(str -> mapproductResponse(str)).collect(Collectors.toList());
    }

    private ProductResponse mapproductResponse(Product str) {
        return ProductResponse.builder()
                .id(str.getId())
                .name(str.getName())
                .description(str.getDescription())
                .price(str.getPrice())
                .build();

    }
}
