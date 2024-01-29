package com.ApiProject.EcommerceBackend.api.security;


import com.ApiProject.EcommerceBackend.model.LocalUser;
import com.ApiProject.EcommerceBackend.model.dao.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class JUnitUserDetailsService implements UserDetailsService {

    @Autowired
    private LocalUserDAO localUserDAO;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isPresent())
            return opUser.get();
        return null;
    }

}
