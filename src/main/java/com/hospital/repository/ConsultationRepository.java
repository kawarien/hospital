package com.hospital.repository;

import com.hospital.domain.Consultation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Consultation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

}
