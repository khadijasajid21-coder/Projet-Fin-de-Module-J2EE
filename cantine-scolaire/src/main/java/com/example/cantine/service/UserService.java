package com.example.cantine.service;

import com.example.cantine.dto.UtilisateurDto;
import com.example.cantine.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserService {
    Page<Utilisateur> findAll(String q, Pageable pageable);
    Optional<Utilisateur> findById(Long id);
    Optional<Utilisateur> findByEmail(String email);
    Utilisateur create(UtilisateurDto dto);
    Utilisateur update(Long id, UtilisateurDto dto);
    void delete(Long id);
    void toggleActif(Long id);
}
