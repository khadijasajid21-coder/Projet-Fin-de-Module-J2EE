package com.example.cantine.dto;

import com.example.cantine.entity.RoleType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UtilisateurDto {
    private Long id;
    @NotBlank @Size(min=2,max=100) private String nom;
    @NotBlank @Size(min=2,max=100) private String prenom;
    @NotBlank @Email private String email;
    private String password;
    @Size(max=20) private String telephone;
    private boolean actif;
    private RoleType role;
    // Etudiant fields
    private String matricule;
    private String filiere;
    private String niveau;
}
