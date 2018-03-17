package com.hospital.web.rest;

import com.hospital.DbHospitalApp;

import com.hospital.domain.Rendezvous;
import com.hospital.repository.RendezvousRepository;
import com.hospital.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.hospital.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RendezvousResource REST controller.
 *
 * @see RendezvousResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbHospitalApp.class)
public class RendezvousResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RendezvousRepository rendezvousRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRendezvousMockMvc;

    private Rendezvous rendezvous;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RendezvousResource rendezvousResource = new RendezvousResource(rendezvousRepository);
        this.restRendezvousMockMvc = MockMvcBuilders.standaloneSetup(rendezvousResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rendezvous createEntity(EntityManager em) {
        Rendezvous rendezvous = new Rendezvous()
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE);
        return rendezvous;
    }

    @Before
    public void initTest() {
        rendezvous = createEntity(em);
    }

    @Test
    @Transactional
    public void createRendezvous() throws Exception {
        int databaseSizeBeforeCreate = rendezvousRepository.findAll().size();

        // Create the Rendezvous
        restRendezvousMockMvc.perform(post("/api/rendezvous")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rendezvous)))
            .andExpect(status().isCreated());

        // Validate the Rendezvous in the database
        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeCreate + 1);
        Rendezvous testRendezvous = rendezvousList.get(rendezvousList.size() - 1);
        assertThat(testRendezvous.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRendezvous.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createRendezvousWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rendezvousRepository.findAll().size();

        // Create the Rendezvous with an existing ID
        rendezvous.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRendezvousMockMvc.perform(post("/api/rendezvous")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rendezvous)))
            .andExpect(status().isBadRequest());

        // Validate the Rendezvous in the database
        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezvousRepository.findAll().size();
        // set the field null
        rendezvous.setDescription(null);

        // Create the Rendezvous, which fails.

        restRendezvousMockMvc.perform(post("/api/rendezvous")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rendezvous)))
            .andExpect(status().isBadRequest());

        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezvousRepository.findAll().size();
        // set the field null
        rendezvous.setDate(null);

        // Create the Rendezvous, which fails.

        restRendezvousMockMvc.perform(post("/api/rendezvous")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rendezvous)))
            .andExpect(status().isBadRequest());

        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRendezvous() throws Exception {
        // Initialize the database
        rendezvousRepository.saveAndFlush(rendezvous);

        // Get all the rendezvousList
        restRendezvousMockMvc.perform(get("/api/rendezvous?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rendezvous.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRendezvous() throws Exception {
        // Initialize the database
        rendezvousRepository.saveAndFlush(rendezvous);

        // Get the rendezvous
        restRendezvousMockMvc.perform(get("/api/rendezvous/{id}", rendezvous.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rendezvous.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRendezvous() throws Exception {
        // Get the rendezvous
        restRendezvousMockMvc.perform(get("/api/rendezvous/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRendezvous() throws Exception {
        // Initialize the database
        rendezvousRepository.saveAndFlush(rendezvous);
        int databaseSizeBeforeUpdate = rendezvousRepository.findAll().size();

        // Update the rendezvous
        Rendezvous updatedRendezvous = rendezvousRepository.findOne(rendezvous.getId());
        // Disconnect from session so that the updates on updatedRendezvous are not directly saved in db
        em.detach(updatedRendezvous);
        updatedRendezvous
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE);

        restRendezvousMockMvc.perform(put("/api/rendezvous")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRendezvous)))
            .andExpect(status().isOk());

        // Validate the Rendezvous in the database
        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeUpdate);
        Rendezvous testRendezvous = rendezvousList.get(rendezvousList.size() - 1);
        assertThat(testRendezvous.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRendezvous.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRendezvous() throws Exception {
        int databaseSizeBeforeUpdate = rendezvousRepository.findAll().size();

        // Create the Rendezvous

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRendezvousMockMvc.perform(put("/api/rendezvous")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rendezvous)))
            .andExpect(status().isCreated());

        // Validate the Rendezvous in the database
        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRendezvous() throws Exception {
        // Initialize the database
        rendezvousRepository.saveAndFlush(rendezvous);
        int databaseSizeBeforeDelete = rendezvousRepository.findAll().size();

        // Get the rendezvous
        restRendezvousMockMvc.perform(delete("/api/rendezvous/{id}", rendezvous.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rendezvous> rendezvousList = rendezvousRepository.findAll();
        assertThat(rendezvousList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rendezvous.class);
        Rendezvous rendezvous1 = new Rendezvous();
        rendezvous1.setId(1L);
        Rendezvous rendezvous2 = new Rendezvous();
        rendezvous2.setId(rendezvous1.getId());
        assertThat(rendezvous1).isEqualTo(rendezvous2);
        rendezvous2.setId(2L);
        assertThat(rendezvous1).isNotEqualTo(rendezvous2);
        rendezvous1.setId(null);
        assertThat(rendezvous1).isNotEqualTo(rendezvous2);
    }
}
