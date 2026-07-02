package com.example.cantine.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MenuDto {
    private Long id;
    @NotNull private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMenu;
    @NotBlank @Size(min=2,max=200) private String titre;
    @Size(max=1000) private String description;
    private boolean actif;
    private List<Long> platIds;
    private List<PlatDto> plats;
}
