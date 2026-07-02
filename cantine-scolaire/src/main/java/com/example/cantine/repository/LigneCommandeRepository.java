package com.example.cantine.repository;

import com.example.cantine.entity.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {

    @Query("SELECT lc.plat.nom, SUM(lc.quantite) as total FROM LigneCommande lc " +
           "GROUP BY lc.plat.id, lc.plat.nom ORDER BY total DESC")
    List<Object[]> findTopPlats();
}
