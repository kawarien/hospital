package com.hospital.repository;

import com.hospital.domain.Facture;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Facture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query("select facture from Facture facture where facture.user.login = ?#{principal.username}")
    List<Facture> findByUserIsCurrentUser();

}
