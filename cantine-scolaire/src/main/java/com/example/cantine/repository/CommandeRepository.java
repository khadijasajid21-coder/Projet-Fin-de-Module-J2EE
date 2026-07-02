package com.example.cantine.repository;

import com.example.cantine.entity.Commande;
import com.example.cantine.entity.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByEtudiantId(Long etudiantId);
    List<Commande> findByEtudiantIdOrderByDateCommandeDesc(Long etudiantId);
    Page<Commande> findByEtudiantId(Long etudiantId, Pageable pageable);
    List<Commande> findByStatut(StatutCommande statut);

    @Query("SELECT c FROM Commande c WHERE " +
           "(:statut IS NULL OR c.statut = :statut) AND " +
           "(:dateDebut IS NULL OR c.dateCommande >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateCommande <= :dateFin) AND " +
           "(:q IS NULL OR LOWER(c.etudiant.utilisateur.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(c.etudiant.matricule) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<Commande> findWithFilters(@Param("statut") StatutCommande statut,
                                   @Param("dateDebut") LocalDateTime dateDebut,
                                   @Param("dateFin") LocalDateTime dateFin,
                                   @Param("q") String q,
                                   Pageable pageable);

    @Query("SELECT COUNT(c) FROM Commande c WHERE DATE(c.dateCommande) = CURRENT_DATE")
    long countAujourdhui();

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.statut != 'ANNULEE'")
    BigDecimal sumMontantTotal();

    @Query("SELECT FUNCTION('DATE', c.dateCommande) as date, COUNT(c) as nb FROM Commande c " +
           "WHERE c.dateCommande >= :debut GROUP BY FUNCTION('DATE', c.dateCommande) ORDER BY FUNCTION('DATE', c.dateCommande)")
    List<Object[]> countByDay(@Param("debut") LocalDateTime debut);

    @Query("SELECT FUNCTION('MONTH', c.dateCommande) as mois, COUNT(c) as nb FROM Commande c " +
           "WHERE FUNCTION('YEAR', c.dateCommande) = FUNCTION('YEAR', CURRENT_DATE) GROUP BY FUNCTION('MONTH', c.dateCommande)")
    List<Object[]> countByMonth();

    @Query("SELECT c.statut, COUNT(c) FROM Commande c GROUP BY c.statut")
    List<Object[]> countByStatut();

    @Query("SELECT FUNCTION('DATE', c.dateCommande) as date, SUM(c.montantTotal) as ca FROM Commande c " +
           "WHERE c.statut != 'ANNULEE' AND c.dateCommande >= :debut " +
           "GROUP BY FUNCTION('DATE', c.dateCommande) ORDER BY FUNCTION('DATE', c.dateCommande)")
    List<Object[]> caByDay(@Param("debut") LocalDateTime debut);

    @Query("SELECT FUNCTION('MONTH', c.dateCommande) as mois, SUM(c.montantTotal) as ca FROM Commande c " +
           "WHERE c.statut != 'ANNULEE' AND FUNCTION('YEAR', c.dateCommande) = FUNCTION('YEAR', CURRENT_DATE) " +
           "GROUP BY FUNCTION('MONTH', c.dateCommande)")
    List<Object[]> caByMonth();
}
