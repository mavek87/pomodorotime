package com.matteoveroni.pomodorotime.configs;

import com.google.gson.Gson;
import com.matteoveroni.pomodorotime.utils.Constants;
import com.matteoveroni.pomodorotime.utils.singleton.ExternalFolderPathSingleton;
import com.matteoveroni.pomodorotime.utils.singleton.GsonSingleton;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Semaphore;

@Slf4j
public enum JsonConfigManager implements ConfigManager {
    INSTANCE;

    private static final String CONFIG_FILE_NAME = "config.json";
    private static final String CONFIG_FILE_TEMPLATE_NAME = "config-template.json";
    private static final String CONFIG_FOLDER_NAME = "config";

    private final Gson gson = GsonSingleton.INSTANCE.getGson();
    private final Path configFile;
    private final Semaphore configFileSemaphore = new Semaphore(1);

    JsonConfigManager() {
        final Path configFolder = Paths.get(System.getProperty("user.home"), Constants.APP_NAME, CONFIG_FOLDER_NAME);
        if (!Files.exists(configFolder)) {
            try {
                Files.createDirectories(configFolder);
            } catch (Exception ex) {
                throw new RuntimeException("Error: impossible to create config directories", ex);
            }
        }
        configFile = Paths.get(configFolder.toAbsolutePath().toString(), CONFIG_FILE_NAME);
        if (!Files.exists(configFile)) {
            try {
                Files.createFile(configFile);
                final Path configFileTemplate = ExternalFolderPathSingleton.INSTANCE.getPath(CONFIG_FOLDER_NAME, CONFIG_FILE_TEMPLATE_NAME);
                final String jsonConfigTemplate = Files.readString(configFileTemplate);
                // TODO: for some reason this doesn't work even when a config-template.json file is put into the config file in the resources
                // final String jsonConfigTemplate = Files.readString(Paths.get(JsonConfigManager.class.getResource("/config/" + CONFIG_FILE_TEMPLATE_NAME).toString()));
                Files.writeString(configFile, jsonConfigTemplate, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception ex) {
                throw new RuntimeException("Error: impossible to write config file", ex);
            }
        }
    }

    @Override
    public Config readConfig() {
        try {
            configFileSemaphore.acquireUninterruptibly();
            return gson.fromJson(Files.readString(configFile), Config.class);
        } catch (IOException ex) {
            log.error("Error", ex);
            throw new RuntimeException("Error: impossible to read config file", ex);
        } finally {
            configFileSemaphore.release();
        }
    }

    @Override
    public void writeConfig(Config config) {
        try {
            configFileSemaphore.acquireUninterruptibly();
            final String jsonConfig = gson.toJson(config);
            log.debug("Writing to file: {} config:\n{}", configFile.toAbsolutePath(), jsonConfig);
            Files.writeString(configFile, jsonConfig, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            log.error("Error", ex);
            throw new RuntimeException("Error: impossible to write to config file");
        } finally {
            configFileSemaphore.release();
        }
    }
}