package com.ApiProject.EcommerceBackend.model.dao;

import com.ApiProject.EcommerceBackend.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {

}
