package com.hospital.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hospital.domain.Rendezvous;

import com.hospital.repository.RendezvousRepository;
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
 * REST controller for managing Rendezvous.
 */
@RestController
@RequestMapping("/api")
public class RendezvousResource {

    private final Logger log = LoggerFactory.getLogger(RendezvousResource.class);

    private static final String ENTITY_NAME = "rendezvous";

    private final RendezvousRepository rendezvousRepository;

    public RendezvousResource(RendezvousRepository rendezvousRepository) {
        this.rendezvousRepository = rendezvousRepository;
    }

    /**
     * POST  /rendezvous : Create a new rendezvous.
     *
     * @param rendezvous the rendezvous to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rendezvous, or with status 400 (Bad Request) if the rendezvous has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rendezvous")
    @Timed
    public ResponseEntity<Rendezvous> createRendezvous(@Valid @RequestBody Rendezvous rendezvous) throws URISyntaxException {
        log.debug("REST request to save Rendezvous : {}", rendezvous);
        if (rendezvous.getId() != null) {
            throw new BadRequestAlertException("A new rendezvous cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rendezvous result = rendezvousRepository.save(rendezvous);
        return ResponseEntity.created(new URI("/api/rendezvous/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rendezvous : Updates an existing rendezvous.
     *
     * @param rendezvous the rendezvous to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rendezvous,
     * or with status 400 (Bad Request) if the rendezvous is not valid,
     * or with status 500 (Internal Server Error) if the rendezvous couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rendezvous")
    @Timed
    public ResponseEntity<Rendezvous> updateRendezvous(@Valid @RequestBody Rendezvous rendezvous) throws URISyntaxException {
        log.debug("REST request to update Rendezvous : {}", rendezvous);
        if (rendezvous.getId() == null) {
            return createRendezvous(rendezvous);
        }
        Rendezvous result = rendezvousRepository.save(rendezvous);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rendezvous.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rendezvous : get all the rendezvous.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rendezvous in body
     */
    @GetMapping("/rendezvous")
    @Timed
    public List<Rendezvous> getAllRendezvous() {
        log.debug("REST request to get all Rendezvous");
        return rendezvousRepository.findAll();
        
        }

    /**
     * GET  /rendezvous/:id : get the "id" rendezvous.
     *
     * @param id the id of the rendezvous to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rendezvous, or with status 404 (Not Found)
     */
    @GetMapping("/rendezvous/{id}")
    @Timed
    public ResponseEntity<Rendezvous> getRendezvous(@PathVariable Long id) {
        log.debug("REST request to get Rendezvous : {}", id);
        Rendezvous rendezvous = rendezvousRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rendezvous));
    }

    /**
     * DELETE  /rendezvous/:id : delete the "id" rendezvous.
     *
     * @param id the id of the rendezvous to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rendezvous/{id}")
    @Timed
    public ResponseEntity<Void> deleteRendezvous(@PathVariable Long id) {
        log.debug("REST request to delete Rendezvous : {}", id);
        rendezvousRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
