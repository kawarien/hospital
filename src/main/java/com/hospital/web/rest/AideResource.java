package com.hospital.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hospital.domain.Aide;

import com.hospital.repository.AideRepository;
import com.hospital.web.rest.errors.BadRequestAlertException;
import com.hospital.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Aide.
 */
@RestController
@RequestMapping("/api")
public class AideResource {

    private final Logger log = LoggerFactory.getLogger(AideResource.class);

    private static final String ENTITY_NAME = "aide";

    private final AideRepository aideRepository;

    public AideResource(AideRepository aideRepository) {
        this.aideRepository = aideRepository;
    }

    /**
     * POST  /aides : Create a new aide.
     *
     * @param aide the aide to create
     * @return the ResponseEntity with status 201 (Created) and with body the new aide, or with status 400 (Bad Request) if the aide has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/aides")
    @Timed
    public ResponseEntity<Aide> createAide(@RequestBody Aide aide) throws URISyntaxException {
        log.debug("REST request to save Aide : {}", aide);
        if (aide.getId() != null) {
            throw new BadRequestAlertException("A new aide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aide result = aideRepository.save(aide);
        return ResponseEntity.created(new URI("/api/aides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /aides : Updates an existing aide.
     *
     * @param aide the aide to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated aide,
     * or with status 400 (Bad Request) if the aide is not valid,
     * or with status 500 (Internal Server Error) if the aide couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/aides")
    @Timed
    public ResponseEntity<Aide> updateAide(@RequestBody Aide aide) throws URISyntaxException {
        log.debug("REST request to update Aide : {}", aide);
        if (aide.getId() == null) {
            return createAide(aide);
        }
        Aide result = aideRepository.save(aide);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, aide.getId().toString()))
            .body(result);
    }

    /**
     * GET  /aides : get all the aides.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of aides in body
     */
    @GetMapping("/aides")
    @Timed
    public List<Aide> getAllAides() {
        log.debug("REST request to get all Aides");
        return aideRepository.findAll();
        }

    /**
     * GET  /aides/:id : get the "id" aide.
     *
     * @param id the id of the aide to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the aide, or with status 404 (Not Found)
     */
    @GetMapping("/aides/{id}")
    @Timed
    public ResponseEntity<Aide> getAide(@PathVariable Long id) {
        log.debug("REST request to get Aide : {}", id);
        Aide aide = aideRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(aide));
    }

    /**
     * DELETE  /aides/:id : delete the "id" aide.
     *
     * @param id the id of the aide to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/aides/{id}")
    @Timed
    public ResponseEntity<Void> deleteAide(@PathVariable Long id) {
        log.debug("REST request to delete Aide : {}", id);
        aideRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
