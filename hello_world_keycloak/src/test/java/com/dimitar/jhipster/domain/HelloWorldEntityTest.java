package com.dimitar.jhipster.domain;

import static com.dimitar.jhipster.domain.HelloWorldEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dimitar.jhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelloWorldEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelloWorldEntity.class);
        HelloWorldEntity helloWorldEntity1 = getHelloWorldEntitySample1();
        HelloWorldEntity helloWorldEntity2 = new HelloWorldEntity();
        assertThat(helloWorldEntity1).isNotEqualTo(helloWorldEntity2);

        helloWorldEntity2.setId(helloWorldEntity1.getId());
        assertThat(helloWorldEntity1).isEqualTo(helloWorldEntity2);

        helloWorldEntity2 = getHelloWorldEntitySample2();
        assertThat(helloWorldEntity1).isNotEqualTo(helloWorldEntity2);
    }
}
