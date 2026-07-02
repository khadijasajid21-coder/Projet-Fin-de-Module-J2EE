package com.example.cantine.repository;

import com.example.cantine.entity.Plat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PlatRepository extends JpaRepository<Plat, Long> {
    List<Plat> findByDisponibleTrue();

    @Query("SELECT p FROM Plat p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Plat> search(String q, Pageable pageable);

    @Query("SELECT p FROM Plat p WHERE " +
           "LOWER(p.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Plat> searchAll(String q, Pageable pageable);
}
