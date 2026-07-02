package com.example.cantine.controller;

import com.example.cantine.dto.MenuDto;
import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller @RequestMapping("/menus") @RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    private final PlatService platService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateMenu").descending());
        model.addAttribute("menus", menuService.findAll(q, pageable));
        model.addAttribute("q", q);
        return "menus/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("menu", new MenuDto());
        model.addAttribute("plats", platService.findDisponibles());
        return "menus/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("menu") MenuDto dto, BindingResult br,
                         Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("plats", platService.findDisponibles());
            return "menus/form";
        }
        menuService.create(dto);
        ra.addFlashAttribute("success", "Menu créé avec succès");
        return "redirect:/menus";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Menu m = menuService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu", id));
        MenuDto dto = new MenuDto();
        dto.setId(m.getId()); dto.setDateMenu(m.getDateMenu());
        dto.setTitre(m.getTitre()); dto.setDescription(m.getDescription()); dto.setActif(m.isActif());
        dto.setPlatIds(m.getMenuPlats().stream().map(mp -> mp.getPlat().getId()).toList());
        model.addAttribute("menu", dto);
        model.addAttribute("plats", platService.findDisponibles());
        return "menus/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("menu") MenuDto dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("plats", platService.findDisponibles());
            return "menus/form";
        }
        menuService.update(id, dto);
        ra.addFlashAttribute("success", "Menu modifié avec succès");
        return "redirect:/menus";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        menuService.delete(id);
        ra.addFlashAttribute("success", "Menu supprimé");
        return "redirect:/menus";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        menuService.toggleActif(id);
        ra.addFlashAttribute("success", "Statut menu modifié");
        return "redirect:/menus";
    }
}
