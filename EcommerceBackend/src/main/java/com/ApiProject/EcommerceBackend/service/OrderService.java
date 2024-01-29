package com.ApiProject.EcommerceBackend.service;

import com.ApiProject.EcommerceBackend.model.LocalUser;
import com.ApiProject.EcommerceBackend.model.WebOrder;
import com.ApiProject.EcommerceBackend.model.dao.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private WebOrderDAO webOrderDAO;

    public OrderService(WebOrderDAO webOrderDAO){
        this.webOrderDAO=webOrderDAO;
    }

    public List<WebOrder> getOrders(LocalUser user){
        return webOrderDAO.findByUser(user);
    }

}
