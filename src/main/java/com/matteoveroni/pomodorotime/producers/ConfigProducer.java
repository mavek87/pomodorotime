package com.matteoveroni.pomodorotime.producers;

import com.google.gson.Gson;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.services.ExternalFolderPathService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
@Slf4j
public class ConfigProducer {

    private final Config config;

    @Inject
    public ConfigProducer(ExternalFolderPathService externalFolderPathService, Gson gson) throws Exception {
        Path configPath = externalFolderPathService.getPath("config", "config.json");
        log.info("config path: {}", configPath);
        this.config = gson.fromJson(Files.readString(configPath), Config.class);
    }

    @Produces
    public Config getConfig() {
        return config;
    }
}
