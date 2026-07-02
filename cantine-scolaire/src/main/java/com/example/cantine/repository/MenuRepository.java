package com.example.cantine.repository;

import com.example.cantine.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByDateMenuAndActifTrue(LocalDate date);
    List<Menu> findByDateMenuAfterAndActifTrue(LocalDate date);
    List<Menu> findByDateMenuGreaterThanEqualAndActifTrueOrderByDateMenuAsc(LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.dateMenu = CURRENT_DATE AND m.actif = true")
    Optional<Menu> findMenuDuJour();

    @Query("SELECT m FROM Menu m WHERE m.dateMenu > CURRENT_DATE AND m.actif = true ORDER BY m.dateMenu ASC")
    List<Menu> findMenusFuturs();

    @Query("SELECT m FROM Menu m WHERE LOWER(m.titre) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Menu> search(String q, Pageable pageable);

    long count();
}
