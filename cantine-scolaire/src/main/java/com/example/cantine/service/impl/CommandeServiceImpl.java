package com.example.cantine.service.impl;

import com.example.cantine.dto.*;
import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.repository.*;
import com.example.cantine.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional
public class CommandeServiceImpl implements CommandeService {
    private final CommandeRepository commandeRepo;
    private final EtudiantRepository etudiantRepo;
    private final PlatRepository platRepo;

    @Override @Transactional(readOnly = true)
    public Page<Commande> findAll(StatutCommande statut, LocalDateTime dateDebut, LocalDateTime dateFin, String q, Pageable pageable) {
        return commandeRepo.findWithFilters(statut, dateDebut, dateFin,
            (q != null && !q.isBlank()) ? q.trim() : null, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<Commande> findById(Long id) { return commandeRepo.findById(id); }

    @Override @Transactional(readOnly = true)
    public List<Commande> findByEtudiant(Long etudiantId) {
        return commandeRepo.findByEtudiantIdOrderByDateCommandeDesc(etudiantId);
    }

    @Override
    public Commande create(CommandeRequest req, Long etudiantId) {
        Etudiant e = etudiantRepo.findByUtilisateurId(etudiantId)
            .orElseThrow(() -> new ResourceNotFoundException("Etudiant avec userId " + etudiantId, 0L));
        Commande c = Commande.builder().etudiant(e).build();
        c = commandeRepo.save(c);
        Commande finalC = c;
        if (req.getLignes() != null) {
            req.getLignes().forEach(l -> {
                Plat p = platRepo.findById(l.getPlatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plat", l.getPlatId()));
                LigneCommande lc = LigneCommande.builder()
                    .quantite(l.getQuantite()).prixUnitaire(p.getPrix())
                    .commande(finalC).plat(p).build();
                finalC.getLignesCommande().add(lc);
            });
        }
        c.calculerTotaux();
        return commandeRepo.save(c);
    }

    @Override
    public Commande changerStatut(Long id, StatutCommande nouveauStatut) {
        Commande c = commandeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commande", id));
        c.setStatut(nouveauStatut);
        return commandeRepo.save(c);
    }

    @Override
    public void delete(Long id) {
        if (!commandeRepo.existsById(id)) throw new ResourceNotFoundException("Commande", id);
        commandeRepo.deleteById(id);
    }
}
