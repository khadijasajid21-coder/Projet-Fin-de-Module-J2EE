package com.example.cantine.repository;

import com.example.cantine.entity.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByMatricule(String matricule);
    Optional<Etudiant> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT e FROM Etudiant e JOIN e.utilisateur u WHERE " +
           "LOWER(e.matricule) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(u.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(u.prenom) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Etudiant> search(String q, Pageable pageable);

    boolean existsByMatricule(String matricule);
}
