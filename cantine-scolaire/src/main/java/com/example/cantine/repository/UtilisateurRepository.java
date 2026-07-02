package com.example.cantine.repository;

import com.example.cantine.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Utilisateur u WHERE " +
           "LOWER(u.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(u.prenom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Utilisateur> search(String q, Pageable pageable);

    @Query("SELECT COUNT(u) FROM Utilisateur u JOIN u.roles r WHERE r.nom = 'ROLE_ETUDIANT'")
    long countEtudiants();
}
