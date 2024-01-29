package com.ApiProject.EcommerceBackend.api.controller.product;


import com.ApiProject.EcommerceBackend.model.Product;
import com.ApiProject.EcommerceBackend.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProduct(){
        return productService.getProduct();
    }
}
