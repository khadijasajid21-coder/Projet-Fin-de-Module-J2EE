package com.example.cantine.service.impl;

import com.example.cantine.dto.DashboardDto;
import com.example.cantine.repository.*;
import com.example.cantine.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final CommandeRepository commandeRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final MenuRepository menuRepo;
    private final LigneCommandeRepository ligneRepo;

    @Override
    public DashboardDto getDashboard() {
        // Top plats
        List<Object[]> topRaw = ligneRepo.findTopPlats();
        List<String> topNoms = new ArrayList<>();
        List<Long> topQtes = new ArrayList<>();
        topRaw.stream().limit(10).forEach(r -> {
            topNoms.add((String) r[0]);
            topQtes.add(((Number) r[1]).longValue());
        });

        // Commandes par jour (30 derniers jours)
        LocalDateTime debut30 = LocalDateTime.now().minusDays(30);
        List<Object[]> parJour = commandeRepo.countByDay(debut30);
        List<String> joursLabels = new ArrayList<>();
        List<Long> joursData = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        parJour.forEach(r -> {
            joursLabels.add(r[0].toString());
            joursData.add(((Number) r[1]).longValue());
        });

        // Commandes par mois
        List<Object[]> parMois = commandeRepo.countByMonth();
        List<String> moisLabels = new ArrayList<>();
        List<Long> moisData = new ArrayList<>();
        String[] moisNoms = {"Jan","Fév","Mar","Avr","Mai","Jun","Jul","Aoû","Sep","Oct","Nov","Déc"};
        parMois.forEach(r -> {
            int m = ((Number) r[0]).intValue();
            moisLabels.add(moisNoms[m-1]);
            moisData.add(((Number) r[1]).longValue());
        });

        // Statuts
        List<Object[]> statuts = commandeRepo.countByStatut();
        Map<String,Long> statutMap = new LinkedHashMap<>();
        statuts.forEach(r -> statutMap.put(r[0].toString(), ((Number) r[1]).longValue()));

        // CA journalier
        List<Object[]> caJour = commandeRepo.caByDay(debut30);
        List<String> caJourLabels = new ArrayList<>();
        List<BigDecimal> caJourData = new ArrayList<>();
        caJour.forEach(r -> {
            caJourLabels.add(r[0].toString());
            caJourData.add(r[1] != null ? new BigDecimal(r[1].toString()) : BigDecimal.ZERO);
        });

        BigDecimal montantTotal = commandeRepo.sumMontantTotal();

        return DashboardDto.builder()
            .totalCommandes(commandeRepo.count())
            .commandesAujourdhui(commandeRepo.countAujourdhui())
            .totalEtudiants(utilisateurRepo.countEtudiants())
            .totalMenus(menuRepo.count())
            .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
            .topPlatsNoms(topNoms).topPlatsQuantites(topQtes)
            .commandesParJourLabels(joursLabels).commandesParJourData(joursData)
            .commandesParMoisLabels(moisLabels).commandesParMoisData(moisData)
            .statutsRepartition(statutMap)
            .caJournalierLabels(caJourLabels).caJournalierData(caJourData)
            .build();
    }
}
