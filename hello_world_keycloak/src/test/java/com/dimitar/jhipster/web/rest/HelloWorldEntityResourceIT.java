package com.dimitar.jhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dimitar.jhipster.IntegrationTest;
import com.dimitar.jhipster.domain.HelloWorldEntity;
import com.dimitar.jhipster.repository.HelloWorldEntityRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HelloWorldEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelloWorldEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TEST_VALUE = false;
    private static final Boolean UPDATED_TEST_VALUE = true;

    private static final String ENTITY_API_URL = "/api/hello-world-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelloWorldEntityRepository helloWorldEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelloWorldEntityMockMvc;

    private HelloWorldEntity helloWorldEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelloWorldEntity createEntity(EntityManager em) {
        HelloWorldEntity helloWorldEntity = new HelloWorldEntity().name(DEFAULT_NAME).testValue(DEFAULT_TEST_VALUE);
        return helloWorldEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelloWorldEntity createUpdatedEntity(EntityManager em) {
        HelloWorldEntity helloWorldEntity = new HelloWorldEntity().name(UPDATED_NAME).testValue(UPDATED_TEST_VALUE);
        return helloWorldEntity;
    }

    @BeforeEach
    public void initTest() {
        helloWorldEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createHelloWorldEntity() throws Exception {
        int databaseSizeBeforeCreate = helloWorldEntityRepository.findAll().size();
        // Create the HelloWorldEntity
        restHelloWorldEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isCreated());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeCreate + 1);
        HelloWorldEntity testHelloWorldEntity = helloWorldEntityList.get(helloWorldEntityList.size() - 1);
        assertThat(testHelloWorldEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelloWorldEntity.getTestValue()).isEqualTo(DEFAULT_TEST_VALUE);
    }

    @Test
    @Transactional
    void createHelloWorldEntityWithExistingId() throws Exception {
        // Create the HelloWorldEntity with an existing ID
        helloWorldEntity.setId(1L);

        int databaseSizeBeforeCreate = helloWorldEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelloWorldEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHelloWorldEntities() throws Exception {
        // Initialize the database
        helloWorldEntityRepository.saveAndFlush(helloWorldEntity);

        // Get all the helloWorldEntityList
        restHelloWorldEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helloWorldEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].testValue").value(hasItem(DEFAULT_TEST_VALUE.booleanValue())));
    }

    @Test
    @Transactional
    void getHelloWorldEntity() throws Exception {
        // Initialize the database
        helloWorldEntityRepository.saveAndFlush(helloWorldEntity);

        // Get the helloWorldEntity
        restHelloWorldEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, helloWorldEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helloWorldEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.testValue").value(DEFAULT_TEST_VALUE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHelloWorldEntity() throws Exception {
        // Get the helloWorldEntity
        restHelloWorldEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelloWorldEntity() throws Exception {
        // Initialize the database
        helloWorldEntityRepository.saveAndFlush(helloWorldEntity);

        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();

        // Update the helloWorldEntity
        HelloWorldEntity updatedHelloWorldEntity = helloWorldEntityRepository.findById(helloWorldEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelloWorldEntity are not directly saved in db
        em.detach(updatedHelloWorldEntity);
        updatedHelloWorldEntity.name(UPDATED_NAME).testValue(UPDATED_TEST_VALUE);

        restHelloWorldEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHelloWorldEntity.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHelloWorldEntity))
            )
            .andExpect(status().isOk());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
        HelloWorldEntity testHelloWorldEntity = helloWorldEntityList.get(helloWorldEntityList.size() - 1);
        assertThat(testHelloWorldEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelloWorldEntity.getTestValue()).isEqualTo(UPDATED_TEST_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingHelloWorldEntity() throws Exception {
        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();
        helloWorldEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelloWorldEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helloWorldEntity.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelloWorldEntity() throws Exception {
        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();
        helloWorldEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelloWorldEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelloWorldEntity() throws Exception {
        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();
        helloWorldEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelloWorldEntityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelloWorldEntityWithPatch() throws Exception {
        // Initialize the database
        helloWorldEntityRepository.saveAndFlush(helloWorldEntity);

        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();

        // Update the helloWorldEntity using partial update
        HelloWorldEntity partialUpdatedHelloWorldEntity = new HelloWorldEntity();
        partialUpdatedHelloWorldEntity.setId(helloWorldEntity.getId());

        partialUpdatedHelloWorldEntity.testValue(UPDATED_TEST_VALUE);

        restHelloWorldEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelloWorldEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelloWorldEntity))
            )
            .andExpect(status().isOk());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
        HelloWorldEntity testHelloWorldEntity = helloWorldEntityList.get(helloWorldEntityList.size() - 1);
        assertThat(testHelloWorldEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelloWorldEntity.getTestValue()).isEqualTo(UPDATED_TEST_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateHelloWorldEntityWithPatch() throws Exception {
        // Initialize the database
        helloWorldEntityRepository.saveAndFlush(helloWorldEntity);

        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();

        // Update the helloWorldEntity using partial update
        HelloWorldEntity partialUpdatedHelloWorldEntity = new HelloWorldEntity();
        partialUpdatedHelloWorldEntity.setId(helloWorldEntity.getId());

        partialUpdatedHelloWorldEntity.name(UPDATED_NAME).testValue(UPDATED_TEST_VALUE);

        restHelloWorldEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelloWorldEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelloWorldEntity))
            )
            .andExpect(status().isOk());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
        HelloWorldEntity testHelloWorldEntity = helloWorldEntityList.get(helloWorldEntityList.size() - 1);
        assertThat(testHelloWorldEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelloWorldEntity.getTestValue()).isEqualTo(UPDATED_TEST_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingHelloWorldEntity() throws Exception {
        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();
        helloWorldEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelloWorldEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helloWorldEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelloWorldEntity() throws Exception {
        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();
        helloWorldEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelloWorldEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelloWorldEntity() throws Exception {
        int databaseSizeBeforeUpdate = helloWorldEntityRepository.findAll().size();
        helloWorldEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelloWorldEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helloWorldEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelloWorldEntity in the database
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelloWorldEntity() throws Exception {
        // Initialize the database
        helloWorldEntityRepository.saveAndFlush(helloWorldEntity);

        int databaseSizeBeforeDelete = helloWorldEntityRepository.findAll().size();

        // Delete the helloWorldEntity
        restHelloWorldEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, helloWorldEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HelloWorldEntity> helloWorldEntityList = helloWorldEntityRepository.findAll();
        assertThat(helloWorldEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
