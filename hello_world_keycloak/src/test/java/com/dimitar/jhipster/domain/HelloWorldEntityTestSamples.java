package com.dimitar.jhipster.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HelloWorldEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HelloWorldEntity getHelloWorldEntitySample1() {
        return new HelloWorldEntity().id(1L).name("name1");
    }

    public static HelloWorldEntity getHelloWorldEntitySample2() {
        return new HelloWorldEntity().id(2L).name("name2");
    }

    public static HelloWorldEntity getHelloWorldEntityRandomSampleGenerator() {
        return new HelloWorldEntity().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
