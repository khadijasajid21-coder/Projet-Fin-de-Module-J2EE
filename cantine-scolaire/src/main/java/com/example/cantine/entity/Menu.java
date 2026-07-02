package com.example.cantine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "menuPlats")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date du menu est obligatoire")
    @Column(name = "date_menu", nullable = false)
    private LocalDate dateMenu;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 2, max = 200)
    @Column(nullable = false)
    private String titre;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private boolean actif = true;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MenuPlat> menuPlats = new ArrayList<>();
}
