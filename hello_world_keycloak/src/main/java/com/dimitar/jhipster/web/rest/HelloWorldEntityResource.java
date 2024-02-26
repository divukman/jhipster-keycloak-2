package com.dimitar.jhipster.web.rest;

import com.dimitar.jhipster.domain.HelloWorldEntity;
import com.dimitar.jhipster.repository.HelloWorldEntityRepository;
import com.dimitar.jhipster.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dimitar.jhipster.domain.HelloWorldEntity}.
 */
@RestController
@RequestMapping("/api/hello-world-entities")
@Transactional
public class HelloWorldEntityResource {

    private final Logger log = LoggerFactory.getLogger(HelloWorldEntityResource.class);

    private static final String ENTITY_NAME = "helloWorldEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelloWorldEntityRepository helloWorldEntityRepository;

    public HelloWorldEntityResource(HelloWorldEntityRepository helloWorldEntityRepository) {
        this.helloWorldEntityRepository = helloWorldEntityRepository;
    }

    /**
     * {@code POST  /hello-world-entities} : Create a new helloWorldEntity.
     *
     * @param helloWorldEntity the helloWorldEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helloWorldEntity, or with status {@code 400 (Bad Request)} if the helloWorldEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HelloWorldEntity> createHelloWorldEntity(@RequestBody HelloWorldEntity helloWorldEntity)
        throws URISyntaxException {
        log.debug("REST request to save HelloWorldEntity : {}", helloWorldEntity);
        if (helloWorldEntity.getId() != null) {
            throw new BadRequestAlertException("A new helloWorldEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HelloWorldEntity result = helloWorldEntityRepository.save(helloWorldEntity);
        return ResponseEntity
            .created(new URI("/api/hello-world-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hello-world-entities/:id} : Updates an existing helloWorldEntity.
     *
     * @param id the id of the helloWorldEntity to save.
     * @param helloWorldEntity the helloWorldEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helloWorldEntity,
     * or with status {@code 400 (Bad Request)} if the helloWorldEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helloWorldEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HelloWorldEntity> updateHelloWorldEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HelloWorldEntity helloWorldEntity
    ) throws URISyntaxException {
        log.debug("REST request to update HelloWorldEntity : {}, {}", id, helloWorldEntity);
        if (helloWorldEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helloWorldEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helloWorldEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HelloWorldEntity result = helloWorldEntityRepository.save(helloWorldEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helloWorldEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hello-world-entities/:id} : Partial updates given fields of an existing helloWorldEntity, field will ignore if it is null
     *
     * @param id the id of the helloWorldEntity to save.
     * @param helloWorldEntity the helloWorldEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helloWorldEntity,
     * or with status {@code 400 (Bad Request)} if the helloWorldEntity is not valid,
     * or with status {@code 404 (Not Found)} if the helloWorldEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the helloWorldEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HelloWorldEntity> partialUpdateHelloWorldEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HelloWorldEntity helloWorldEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelloWorldEntity partially : {}, {}", id, helloWorldEntity);
        if (helloWorldEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helloWorldEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helloWorldEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HelloWorldEntity> result = helloWorldEntityRepository
            .findById(helloWorldEntity.getId())
            .map(existingHelloWorldEntity -> {
                if (helloWorldEntity.getName() != null) {
                    existingHelloWorldEntity.setName(helloWorldEntity.getName());
                }
                if (helloWorldEntity.getTestValue() != null) {
                    existingHelloWorldEntity.setTestValue(helloWorldEntity.getTestValue());
                }

                return existingHelloWorldEntity;
            })
            .map(helloWorldEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helloWorldEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /hello-world-entities} : get all the helloWorldEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helloWorldEntities in body.
     */
    @GetMapping("")
    public List<HelloWorldEntity> getAllHelloWorldEntities() {
        log.debug("REST request to get all HelloWorldEntities");
        return helloWorldEntityRepository.findAll();
    }

    /**
     * {@code GET  /hello-world-entities/:id} : get the "id" helloWorldEntity.
     *
     * @param id the id of the helloWorldEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helloWorldEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HelloWorldEntity> getHelloWorldEntity(@PathVariable("id") Long id) {
        log.debug("REST request to get HelloWorldEntity : {}", id);
        Optional<HelloWorldEntity> helloWorldEntity = helloWorldEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(helloWorldEntity);
    }

    /**
     * {@code DELETE  /hello-world-entities/:id} : delete the "id" helloWorldEntity.
     *
     * @param id the id of the helloWorldEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHelloWorldEntity(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelloWorldEntity : {}", id);
        helloWorldEntityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
