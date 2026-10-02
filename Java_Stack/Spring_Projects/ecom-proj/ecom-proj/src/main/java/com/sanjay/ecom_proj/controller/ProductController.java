package com.sanjay.ecom_proj.controller;

import com.sanjay.ecom_proj.model.Product;
import com.sanjay.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService service;

//    public ProductController(ProductService service) {
//        this.service = service;
//    }

    @RequestMapping("/")
    public String greet(){
        return "hello world";
    }
    @GetMapping("/products")
    public List<Product> getAllProduct(){
        return service.getAllProducts();
    }
}
