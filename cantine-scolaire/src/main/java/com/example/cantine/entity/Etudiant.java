package com.example.cantine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "commandes")
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    @Column(nullable = false, unique = true)
    private String matricule;

    @NotBlank(message = "La filière est obligatoire")
    private String filiere;

    @NotBlank(message = "Le niveau est obligatoire")
    private String niveau;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private List<Commande> commandes;
}
