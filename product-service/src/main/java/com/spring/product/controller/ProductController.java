package com.spring.product.controller;

import com.spring.product.entity.Product;
import com.spring.product.serv.ProductServ;
import com.spring.product.vo.ProductResponse;
import com.spring.product.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductServ productServ;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody ProductVo productVo) {
        productServ.save(productVo);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productServ.getAllProducts();
    }

    @GetMapping(value = "/getById")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getById(@Param("id") String id) {
        Product product = productServ.findById(id);
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();

    }


}
