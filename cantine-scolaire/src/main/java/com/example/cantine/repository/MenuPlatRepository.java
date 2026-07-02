package com.example.cantine.repository;

import com.example.cantine.entity.MenuPlat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuPlatRepository extends JpaRepository<MenuPlat, Long> {
    List<MenuPlat> findByMenuId(Long menuId);
    void deleteByMenuId(Long menuId);
}
