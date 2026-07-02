package com.example.cantine.service;

import com.example.cantine.dto.CommandeDto;
import com.example.cantine.dto.CommandeRequest;
import com.example.cantine.entity.Commande;
import com.example.cantine.entity.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommandeService {
    Page<Commande> findAll(StatutCommande statut, LocalDateTime dateDebut, LocalDateTime dateFin, String q, Pageable pageable);
    Optional<Commande> findById(Long id);
    List<Commande> findByEtudiant(Long etudiantId);
    Commande create(CommandeRequest req, Long etudiantId);
    Commande changerStatut(Long id, StatutCommande nouveauStatut);
    void delete(Long id);
}
