package com.hospital.repository;

import com.hospital.domain.Rendezvous;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Rendezvous entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Long> {

    @Query("select rendezvous from Rendezvous rendezvous where rendezvous.user.login = ?#{principal.username}")
    List<Rendezvous> findByUserIsCurrentUser();

}
