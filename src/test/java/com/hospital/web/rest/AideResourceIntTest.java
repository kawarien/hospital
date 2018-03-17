package com.hospital.web.rest;

import com.hospital.DbHospitalApp;

import com.hospital.domain.Aide;
import com.hospital.repository.AideRepository;
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
import java.util.List;

import static com.hospital.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AideResource REST controller.
 *
 * @see AideResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbHospitalApp.class)
public class AideResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AideRepository aideRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAideMockMvc;

    private Aide aide;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AideResource aideResource = new AideResource(aideRepository);
        this.restAideMockMvc = MockMvcBuilders.standaloneSetup(aideResource)
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
    public static Aide createEntity(EntityManager em) {
        Aide aide = new Aide()
            .description(DEFAULT_DESCRIPTION);
        return aide;
    }

    @Before
    public void initTest() {
        aide = createEntity(em);
    }

    @Test
    @Transactional
    public void createAide() throws Exception {
        int databaseSizeBeforeCreate = aideRepository.findAll().size();

        // Create the Aide
        restAideMockMvc.perform(post("/api/aides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aide)))
            .andExpect(status().isCreated());

        // Validate the Aide in the database
        List<Aide> aideList = aideRepository.findAll();
        assertThat(aideList).hasSize(databaseSizeBeforeCreate + 1);
        Aide testAide = aideList.get(aideList.size() - 1);
        assertThat(testAide.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aideRepository.findAll().size();

        // Create the Aide with an existing ID
        aide.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAideMockMvc.perform(post("/api/aides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aide)))
            .andExpect(status().isBadRequest());

        // Validate the Aide in the database
        List<Aide> aideList = aideRepository.findAll();
        assertThat(aideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAides() throws Exception {
        // Initialize the database
        aideRepository.saveAndFlush(aide);

        // Get all the aideList
        restAideMockMvc.perform(get("/api/aides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aide.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAide() throws Exception {
        // Initialize the database
        aideRepository.saveAndFlush(aide);

        // Get the aide
        restAideMockMvc.perform(get("/api/aides/{id}", aide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aide.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAide() throws Exception {
        // Get the aide
        restAideMockMvc.perform(get("/api/aides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAide() throws Exception {
        // Initialize the database
        aideRepository.saveAndFlush(aide);
        int databaseSizeBeforeUpdate = aideRepository.findAll().size();

        // Update the aide
        Aide updatedAide = aideRepository.findOne(aide.getId());
        // Disconnect from session so that the updates on updatedAide are not directly saved in db
        em.detach(updatedAide);
        updatedAide
            .description(UPDATED_DESCRIPTION);

        restAideMockMvc.perform(put("/api/aides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAide)))
            .andExpect(status().isOk());

        // Validate the Aide in the database
        List<Aide> aideList = aideRepository.findAll();
        assertThat(aideList).hasSize(databaseSizeBeforeUpdate);
        Aide testAide = aideList.get(aideList.size() - 1);
        assertThat(testAide.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAide() throws Exception {
        int databaseSizeBeforeUpdate = aideRepository.findAll().size();

        // Create the Aide

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAideMockMvc.perform(put("/api/aides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aide)))
            .andExpect(status().isCreated());

        // Validate the Aide in the database
        List<Aide> aideList = aideRepository.findAll();
        assertThat(aideList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAide() throws Exception {
        // Initialize the database
        aideRepository.saveAndFlush(aide);
        int databaseSizeBeforeDelete = aideRepository.findAll().size();

        // Get the aide
        restAideMockMvc.perform(delete("/api/aides/{id}", aide.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Aide> aideList = aideRepository.findAll();
        assertThat(aideList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aide.class);
        Aide aide1 = new Aide();
        aide1.setId(1L);
        Aide aide2 = new Aide();
        aide2.setId(aide1.getId());
        assertThat(aide1).isEqualTo(aide2);
        aide2.setId(2L);
        assertThat(aide1).isNotEqualTo(aide2);
        aide1.setId(null);
        assertThat(aide1).isNotEqualTo(aide2);
    }
}
