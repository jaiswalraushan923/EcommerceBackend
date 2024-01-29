package com.ApiProject.EcommerceBackend.service;

import com.ApiProject.EcommerceBackend.model.Product;
import com.ApiProject.EcommerceBackend.model.dao.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO){
        this.productDAO=productDAO;
    }

    public List<Product> getProduct(){
        return productDAO.findAll();
    }

}
