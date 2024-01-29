package com.ApiProject.EcommerceBackend.model.dao;

import com.ApiProject.EcommerceBackend.model.LocalUser;
import com.ApiProject.EcommerceBackend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken,Long> {
    List<VerificationToken> findByUser_IdOrderByIdDesc(Long id);
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);


}
