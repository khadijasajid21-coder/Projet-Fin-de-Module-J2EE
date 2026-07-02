package com.example.cantine.controller;

import com.example.cantine.dto.UtilisateurDto;
import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/users") @RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        model.addAttribute("users", userService.findAll(q, pageable));
        model.addAttribute("q", q);
        model.addAttribute("roles", RoleType.values());
        return "users/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("user", new UtilisateurDto());
        model.addAttribute("roles", RoleType.values());
        return "users/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("user") UtilisateurDto dto, BindingResult br,
                         Model model, RedirectAttributes ra) {
        if (br.hasErrors()) { model.addAttribute("roles", RoleType.values()); return "users/form"; }
        userService.create(dto);
        ra.addFlashAttribute("success", "Utilisateur créé avec succès");
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Utilisateur u = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        UtilisateurDto dto = new UtilisateurDto();
        dto.setId(u.getId()); dto.setNom(u.getNom()); dto.setPrenom(u.getPrenom());
        dto.setEmail(u.getEmail()); dto.setTelephone(u.getTelephone()); dto.setActif(u.isActif());
        u.getRoles().stream().findFirst().ifPresent(r -> dto.setRole(r.getNom()));
        if (u.getEtudiant() != null) {
            dto.setMatricule(u.getEtudiant().getMatricule());
            dto.setFiliere(u.getEtudiant().getFiliere());
            dto.setNiveau(u.getEtudiant().getNiveau());
        }
        model.addAttribute("user", dto);
        model.addAttribute("roles", RoleType.values());
        return "users/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("user") UtilisateurDto dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) { model.addAttribute("roles", RoleType.values()); return "users/form"; }
        userService.update(id, dto);
        ra.addFlashAttribute("success", "Utilisateur modifié");
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        userService.delete(id);
        ra.addFlashAttribute("success", "Utilisateur supprimé");
        return "redirect:/users";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleActif(id);
        ra.addFlashAttribute("success", "Statut modifié");
        return "redirect:/users";
    }
}
