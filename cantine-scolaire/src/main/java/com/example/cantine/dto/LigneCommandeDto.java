package com.example.cantine.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LigneCommandeDto {
    private Long id;
    @Positive private Integer quantite;
    private BigDecimal prixUnitaire;
    private Long platId;
    private String platNom;
    private BigDecimal sousTotal;
}
