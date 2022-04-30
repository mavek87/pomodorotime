package com.matteoveroni.pomodorotime.producers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ClassLoaderProducer {

    @Produces
    public ClassLoader getClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }
}
