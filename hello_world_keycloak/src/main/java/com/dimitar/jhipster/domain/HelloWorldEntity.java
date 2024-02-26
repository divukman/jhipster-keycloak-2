package com.dimitar.jhipster.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A HelloWorldEntity.
 */
@Entity
@Table(name = "hello_world_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelloWorldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "test_value")
    private Boolean testValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelloWorldEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HelloWorldEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTestValue() {
        return this.testValue;
    }

    public HelloWorldEntity testValue(Boolean testValue) {
        this.setTestValue(testValue);
        return this;
    }

    public void setTestValue(Boolean testValue) {
        this.testValue = testValue;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelloWorldEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((HelloWorldEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelloWorldEntity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", testValue='" + getTestValue() + "'" +
            "}";
    }
}
