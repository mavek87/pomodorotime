package com.matteoveroni.pomodorotime.singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum GsonSingleton {
    INSTANCE;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Gson getGson() {
        return gson;
    }
}
