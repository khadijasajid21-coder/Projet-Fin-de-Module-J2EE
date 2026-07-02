package com.example.cantine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "plats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"menuPlats","lignesCommande"})
public class Plat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du plat est obligatoire")
    @Size(min = 2, max = 200)
    @Column(nullable = false)
    private String nom;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    @Builder.Default
    private boolean disponible = true;

    private String image;

    @OneToMany(mappedBy = "plat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuPlat> menuPlats;

    @OneToMany(mappedBy = "plat")
    private List<LigneCommande> lignesCommande;
}
