package com.matteoveroni.pomodorotime.producers;

import com.google.gson.Gson;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.utils.ExternalFolderPath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class ConfigProducer {

    private final Config config;

    @Inject
    public ConfigProducer(Gson gson) throws Exception {
        Path configPath = ExternalFolderPath.getPath("config", "config.json");
        Logger log = LoggerFactory.getLogger(getClass());
        log.info("configPath {}", configPath);
        this.config = gson.fromJson(Files.readString(configPath), Config.class);
    }

    @Produces
    public Config getConfig() {
        return config;
    }
}
