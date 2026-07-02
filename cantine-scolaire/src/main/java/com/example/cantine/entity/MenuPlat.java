package com.example.cantine.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_plats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"menu","plat"})
public class MenuPlat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plat_id", nullable = false)
    private Plat plat;
}
