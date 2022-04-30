package com.matteoveroni.pomodorotime.producers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class GsonProducer {

    @Produces
    public Gson getGson() {
        return new GsonBuilder().create();
    }
}
