package com.example.cantine.controller;

import com.example.cantine.dto.PlatDto;
import com.example.cantine.entity.Plat;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.service.PlatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/plats") @RequiredArgsConstructor
public class PlatController {
    private final PlatService platService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        model.addAttribute("plats", platService.findAll(q, pageable));
        model.addAttribute("q", q);
        return "plats/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("plat", new PlatDto());
        return "plats/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("plat") PlatDto dto, BindingResult br,
                         @RequestParam(required = false) MultipartFile imageFile,
                         Model model, RedirectAttributes ra) {
        if (br.hasErrors()) return "plats/form";
        platService.create(dto, imageFile);
        ra.addFlashAttribute("success", "Plat créé avec succès");
        return "redirect:/plats";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Plat p = platService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plat", id));
        PlatDto dto = new PlatDto();
        dto.setId(p.getId()); dto.setNom(p.getNom()); dto.setDescription(p.getDescription());
        dto.setPrix(p.getPrix()); dto.setDisponible(p.isDisponible()); dto.setImage(p.getImage());
        model.addAttribute("plat", dto);
        return "plats/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("plat") PlatDto dto,
                         BindingResult br, @RequestParam(required = false) MultipartFile imageFile,
                         Model model, RedirectAttributes ra) {
        if (br.hasErrors()) return "plats/form";
        platService.update(id, dto, imageFile);
        ra.addFlashAttribute("success", "Plat modifié avec succès");
        return "redirect:/plats";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        platService.delete(id);
        ra.addFlashAttribute("success", "Plat supprimé");
        return "redirect:/plats";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        platService.toggleDisponible(id);
        ra.addFlashAttribute("success", "Disponibilité modifiée");
        return "redirect:/plats";
    }
}
