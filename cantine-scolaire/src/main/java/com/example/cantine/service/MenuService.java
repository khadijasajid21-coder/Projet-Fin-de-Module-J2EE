package com.example.cantine.service;

import com.example.cantine.dto.MenuDto;
import com.example.cantine.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    Page<Menu> findAll(String q, Pageable pageable);
    Optional<Menu> findById(Long id);
    Optional<Menu> findMenuDuJour();
    List<Menu> findMenusFuturs();
    Menu create(MenuDto dto);
    Menu update(Long id, MenuDto dto);
    void delete(Long id);
    void toggleActif(Long id);
    void addPlats(Long menuId, List<Long> platIds);
    void removePlat(Long menuId, Long platId);
}
