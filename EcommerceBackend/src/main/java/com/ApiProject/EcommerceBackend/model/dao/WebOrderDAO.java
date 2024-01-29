package com.ApiProject.EcommerceBackend.model.dao;

import com.ApiProject.EcommerceBackend.model.LocalUser;
import com.ApiProject.EcommerceBackend.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder,Long> {
    List<WebOrder> findByUser(LocalUser user);
}
