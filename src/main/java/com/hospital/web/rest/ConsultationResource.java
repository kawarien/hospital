package com.hospital.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hospital.domain.Consultation;

import com.hospital.repository.ConsultationRepository;
import com.hospital.web.rest.errors.BadRequestAlertException;
import com.hospital.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Consultation.
 */
@RestController
@RequestMapping("/api")
public class ConsultationResource {

    private final Logger log = LoggerFactory.getLogger(ConsultationResource.class);

    private static final String ENTITY_NAME = "consultation";

    private final ConsultationRepository consultationRepository;

    public ConsultationResource(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    /**
     * POST  /consultations : Create a new consultation.
     *
     * @param consultation the consultation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new consultation, or with status 400 (Bad Request) if the consultation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/consultations")
    @Timed
    public ResponseEntity<Consultation> createConsultation(@Valid @RequestBody Consultation consultation) throws URISyntaxException {
        log.debug("REST request to save Consultation : {}", consultation);
        if (consultation.getId() != null) {
            throw new BadRequestAlertException("A new consultation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Consultation result = consultationRepository.save(consultation);
        return ResponseEntity.created(new URI("/api/consultations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /consultations : Updates an existing consultation.
     *
     * @param consultation the consultation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated consultation,
     * or with status 400 (Bad Request) if the consultation is not valid,
     * or with status 500 (Internal Server Error) if the consultation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/consultations")
    @Timed
    public ResponseEntity<Consultation> updateConsultation(@Valid @RequestBody Consultation consultation) throws URISyntaxException {
        log.debug("REST request to update Consultation : {}", consultation);
        if (consultation.getId() == null) {
            return createConsultation(consultation);
        }
        Consultation result = consultationRepository.save(consultation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, consultation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /consultations : get all the consultations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of consultations in body
     */
    @GetMapping("/consultations")
    @Timed
    public List<Consultation> getAllConsultations() {
        log.debug("REST request to get all Consultations");
        return consultationRepository.findAll();
        }

    /**
     * GET  /consultations/:id : get the "id" consultation.
     *
     * @param id the id of the consultation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the consultation, or with status 404 (Not Found)
     */
    @GetMapping("/consultations/{id}")
    @Timed
    public ResponseEntity<Consultation> getConsultation(@PathVariable Long id) {
        log.debug("REST request to get Consultation : {}", id);
        Consultation consultation = consultationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(consultation));
    }

    /**
     * DELETE  /consultations/:id : delete the "id" consultation.
     *
     * @param id the id of the consultation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/consultations/{id}")
    @Timed
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        log.debug("REST request to delete Consultation : {}", id);
        consultationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
