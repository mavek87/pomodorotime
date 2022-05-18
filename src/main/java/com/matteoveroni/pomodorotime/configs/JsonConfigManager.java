package com.matteoveroni.pomodorotime.configs;

import com.google.gson.Gson;
import com.matteoveroni.pomodorotime.utils.Constants;
import com.matteoveroni.pomodorotime.utils.FileUtils;
import com.matteoveroni.pomodorotime.singleton.GsonSingleton;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
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
                final String configTemplatePath = CONFIG_FOLDER_NAME + "/" + CONFIG_FILE_TEMPLATE_NAME;
                final String jsonConfigTemplate = FileUtils.readFromInputStream(JsonConfigManager.class.getResourceAsStream(configTemplatePath));
                LoggerFactory.getLogger(JsonConfigManager.class).debug("read from {} jsonConfigTemplate {}", configTemplatePath, jsonConfigTemplate);
                Files.writeString(configFile, jsonConfigTemplate, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception ex) {
                throw new RuntimeException("Error: impossible to create config file using the configTemplate", ex);
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