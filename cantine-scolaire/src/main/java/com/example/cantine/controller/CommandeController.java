package com.example.cantine.controller;

import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller @RequestMapping("/commandes") @RequiredArgsConstructor
public class CommandeController {
    private final CommandeService commandeService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String statut,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
                       @RequestParam(required = false) String q) {
        StatutCommande sc = null;
        if (statut != null && !statut.isBlank()) sc = StatutCommande.valueOf(statut);
        LocalDateTime dd = dateDebut != null ? dateDebut.atStartOfDay() : null;
        LocalDateTime df = dateFin != null ? dateFin.atTime(23,59,59) : null;
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCommande").descending());
        model.addAttribute("commandes", commandeService.findAll(sc, dd, df, q, pageable));
        model.addAttribute("statuts", StatutCommande.values());
        model.addAttribute("statutSelectionne", statut);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("q", q);
        return "commandes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Commande c = commandeService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commande", id));
        model.addAttribute("commande", c);
        model.addAttribute("statuts", StatutCommande.values());
        return "commandes/details";
    }

    @PostMapping("/{id}/statut")
    public String changerStatut(@PathVariable Long id, @RequestParam String statut, RedirectAttributes ra) {
        commandeService.changerStatut(id, StatutCommande.valueOf(statut));
        ra.addFlashAttribute("success", "Statut mis à jour");
        return "redirect:/commandes/" + id;
    }
}
