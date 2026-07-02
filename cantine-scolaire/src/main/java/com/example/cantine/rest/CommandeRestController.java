package com.example.cantine.rest;

import com.example.cantine.dto.*;
import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.security.UserDetailsImpl;
import com.example.cantine.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Commandes", description = "API commandes pour Android")
@RestController @RequestMapping("/api/commandes") @RequiredArgsConstructor
public class CommandeRestController {
    private final CommandeService commandeService;

    @Operation(summary = "Créer une commande")
    @PostMapping
    public ResponseEntity<CommandeDto> create(@Valid @RequestBody CommandeRequest req,
                                              @AuthenticationPrincipal UserDetailsImpl ud) {
        Commande c = commandeService.create(req, ud.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(c));
    }

    @Operation(summary = "Historique des commandes de l'étudiant connecté")
    @GetMapping("/history")
    public ResponseEntity<List<CommandeDto>> history(@AuthenticationPrincipal UserDetailsImpl ud) {
        return ResponseEntity.ok(commandeService.findByEtudiant(ud.getId())
            .stream().map(this::toDto).collect(Collectors.toList()));
    }

    @Operation(summary = "Statut d'une commande")
    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String,String>> status(@PathVariable Long id) {
        Commande c = commandeService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commande", id));
        return ResponseEntity.ok(Map.of("id", String.valueOf(id), "statut", c.getStatut().name()));
    }

    @Operation(summary = "Détail d'une commande")
    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> detail(@PathVariable Long id) {
        return commandeService.findById(id)
            .map(c -> ResponseEntity.ok(toDto(c)))
            .orElseThrow(() -> new ResourceNotFoundException("Commande", id));
    }

    private CommandeDto toDto(Commande c) {
        List<LigneCommandeDto> lignes = c.getLignesCommande().stream().map(l ->
            LigneCommandeDto.builder()
                .id(l.getId()).quantite(l.getQuantite()).prixUnitaire(l.getPrixUnitaire())
                .platId(l.getPlat().getId()).platNom(l.getPlat().getNom())
                .sousTotal(l.getSousTotal()).build()
        ).toList();
        return CommandeDto.builder()
            .id(c.getId()).dateCommande(c.getDateCommande()).statut(c.getStatut())
            .quantiteTotale(c.getQuantiteTotale()).montantTotal(c.getMontantTotal())
            .etudiantId(c.getEtudiant().getId())
            .etudiantNom(c.getEtudiant().getUtilisateur().getNomComplet())
            .etudiantMatricule(c.getEtudiant().getMatricule())
            .lignes(lignes).build();
    }
}
