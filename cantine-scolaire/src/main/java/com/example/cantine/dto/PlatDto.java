package com.example.cantine.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PlatDto {
    private Long id;
    @NotBlank @Size(min=2,max=200) private String nom;
    @Size(max=1000) private String description;
    @NotNull @Positive private BigDecimal prix;
    private boolean disponible;
    private String image;
}
