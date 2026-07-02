package com.example.cantine.service.impl;

import com.example.cantine.dto.MenuDto;
import com.example.cantine.entity.*;
import com.example.cantine.exception.ResourceNotFoundException;
import com.example.cantine.repository.*;
import com.example.cantine.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepo;
    private final PlatRepository platRepo;
    private final MenuPlatRepository menuPlatRepo;

    @Override @Transactional(readOnly = true)
    public Page<Menu> findAll(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) return menuRepo.search(q.trim(), pageable);
        return menuRepo.findAll(pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<Menu> findById(Long id) { return menuRepo.findById(id); }

    @Override @Transactional(readOnly = true)
    public Optional<Menu> findMenuDuJour() { return menuRepo.findMenuDuJour(); }

    @Override @Transactional(readOnly = true)
    public List<Menu> findMenusFuturs() { return menuRepo.findMenusFuturs(); }

    @Override
    public Menu create(MenuDto dto) {
        Menu m = Menu.builder()
            .dateMenu(dto.getDateMenu()).titre(dto.getTitre())
            .description(dto.getDescription()).actif(dto.isActif()).build();
        m = menuRepo.save(m);
        if (dto.getPlatIds() != null) addPlats(m.getId(), dto.getPlatIds());
        return m;
    }

    @Override
    public Menu update(Long id, MenuDto dto) {
        Menu m = menuRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu", id));
        m.setDateMenu(dto.getDateMenu()); m.setTitre(dto.getTitre());
        m.setDescription(dto.getDescription()); m.setActif(dto.isActif());
        menuPlatRepo.deleteByMenuId(id);
        m.getMenuPlats().clear();
        if (dto.getPlatIds() != null) {
            Menu saved = menuRepo.save(m);
            addPlats(saved.getId(), dto.getPlatIds());
            return saved;
        }
        return menuRepo.save(m);
    }

    @Override
    public void delete(Long id) {
        if (!menuRepo.existsById(id)) throw new ResourceNotFoundException("Menu", id);
        menuRepo.deleteById(id);
    }

    @Override
    public void toggleActif(Long id) {
        Menu m = menuRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu", id));
        m.setActif(!m.isActif());
        menuRepo.save(m);
    }

    @Override
    public void addPlats(Long menuId, List<Long> platIds) {
        Menu m = menuRepo.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", menuId));
        platIds.forEach(pid -> {
            Plat p = platRepo.findById(pid).orElseThrow(() -> new ResourceNotFoundException("Plat", pid));
            MenuPlat mp = MenuPlat.builder().menu(m).plat(p).build();
            menuPlatRepo.save(mp);
        });
    }

    @Override
    public void removePlat(Long menuId, Long platId) {
        menuPlatRepo.findByMenuId(menuId).stream()
            .filter(mp -> mp.getPlat().getId().equals(platId))
            .forEach(menuPlatRepo::delete);
    }
}
