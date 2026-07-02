package com.example.cantine.rest;

import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Menus", description = "API menus pour Android")
@RestController @RequestMapping("/api/menus") @RequiredArgsConstructor
public class MenuRestController {
    private final MenuService menuService;

    @Operation(summary = "Tous les menus")
    @GetMapping
    public ResponseEntity<List<Menu>> all() {
        return ResponseEntity.ok(menuService.findAll(null, org.springframework.data.domain.Pageable.unpaged()).getContent());
    }

    @Operation(summary = "Menu du jour")
    @GetMapping("/today")
    public ResponseEntity<Menu> today() {
        return menuService.findMenuDuJour()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Menus futurs")
    @GetMapping("/future")
    public ResponseEntity<List<Menu>> future() {
        return ResponseEntity.ok(menuService.findMenusFuturs());
    }

    @Operation(summary = "Détail d'un menu")
    @GetMapping("/{id}")
    public ResponseEntity<Menu> detail(@PathVariable Long id) {
        return menuService.findById(id)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Menu", id));
    }
}
