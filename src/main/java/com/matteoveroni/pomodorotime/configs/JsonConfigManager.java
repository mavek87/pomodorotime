package com.matteoveroni.pomodorotime.configs;

import com.google.gson.Gson;
import com.matteoveroni.pomodorotime.utils.singleton.ExternalFolderPathSingleton;
import com.matteoveroni.pomodorotime.utils.singleton.GsonSingleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Semaphore;

public enum JsonConfigManager implements ConfigManager {
    INSTANCE;

    private static final Path configPath = ExternalFolderPathSingleton.INSTANCE.getPath("config", "config.json");
    private static final Gson gson = GsonSingleton.INSTANCE.getGson();

    private final Semaphore configFileSemaphore = new Semaphore(1);

    @Override
    public Config readConfig() {
        try {
            configFileSemaphore.acquireUninterruptibly();
            return gson.fromJson(Files.readString(configPath), Config.class);
        } catch (IOException ex) {
            throw new RuntimeException("Error trying to read from config file", ex);
        } finally {
            configFileSemaphore.release();
        }
    }

    @Override
    public void writeConfig(Config config) {
        try {
            configFileSemaphore.acquireUninterruptibly();
            Files.writeString(configPath, gson.toJson(config), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Error trying to write to config file");
        } finally {
            configFileSemaphore.release();
        }
    }
}