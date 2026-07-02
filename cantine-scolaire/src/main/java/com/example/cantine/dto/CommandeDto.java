package com.example.cantine.dto;

import com.example.cantine.entity.StatutCommande;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CommandeDto {
    private Long id;
    private LocalDateTime dateCommande;
    private StatutCommande statut;
    private Integer quantiteTotale;
    private BigDecimal montantTotal;
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantMatricule;
    private List<LigneCommandeDto> lignes;
}
