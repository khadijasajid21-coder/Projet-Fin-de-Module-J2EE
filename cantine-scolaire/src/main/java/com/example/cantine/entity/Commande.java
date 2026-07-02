package com.example.cantine.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"lignesCommande","etudiant"})
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_commande", nullable = false)
    @Builder.Default
    private LocalDateTime dateCommande = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @Column(name = "quantite_totale")
    @Builder.Default
    private Integer quantiteTotale = 0;

    @Column(name = "montant_total", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LigneCommande> lignesCommande = new ArrayList<>();

    public void calculerTotaux() {
        this.quantiteTotale = lignesCommande.stream().mapToInt(LigneCommande::getQuantite).sum();
        this.montantTotal = lignesCommande.stream()
            .map(l -> l.getPrixUnitaire().multiply(BigDecimal.valueOf(l.getQuantite())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
