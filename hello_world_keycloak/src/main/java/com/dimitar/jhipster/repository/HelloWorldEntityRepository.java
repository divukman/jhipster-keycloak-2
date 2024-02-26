package com.dimitar.jhipster.repository;

import com.dimitar.jhipster.domain.HelloWorldEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HelloWorldEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelloWorldEntityRepository extends JpaRepository<HelloWorldEntity, Long> {}
