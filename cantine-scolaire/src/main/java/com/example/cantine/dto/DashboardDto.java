package com.example.cantine.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardDto {
    private long totalCommandes;
    private long commandesAujourdhui;
    private long totalEtudiants;
    private long totalMenus;
    private BigDecimal montantTotal;
    private List<String> topPlatsNoms;
    private List<Long> topPlatsQuantites;
    private List<String> commandesParJourLabels;
    private List<Long> commandesParJourData;
    private List<String> commandesParMoisLabels;
    private List<Long> commandesParMoisData;
    private Map<String, Long> statutsRepartition;
    private List<String> caJournalierLabels;
    private List<BigDecimal> caJournalierData;
}
