package com.matteoveroni.pomodorotime.singleton;

import com.matteoveroni.pomodorotime.configs.Config;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;

public enum ConfigSingleton {
    INSTANCE;

    private final Config config;

    ConfigSingleton() {
        try {
            final Path configPath = ExternalFolderPathSingleton.INSTANCE.getPath("config", "config.json");
            LoggerFactory.getLogger(ConfigSingleton.class).info("config path: {}", configPath);
            this.config = GsonSingleton.INSTANCE.getGson().fromJson(Files.readString(configPath), Config.class);
        } catch (Exception ex) {
            throw new RuntimeException("Error", ex);
        }
    }

    public Config getConfig() {
        return config;
    }
}
