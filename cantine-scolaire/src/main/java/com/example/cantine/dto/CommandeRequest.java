package com.example.cantine.dto;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CommandeRequest {
    private List<LigneCommandeDto> lignes;
}
