package com.hospital.repository;

import com.hospital.domain.Aide;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Aide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AideRepository extends JpaRepository<Aide, Long> {

    @Query("select aide from Aide aide where aide.user.login = ?#{principal.username}")
    List<Aide> findByUserIsCurrentUser();

}
