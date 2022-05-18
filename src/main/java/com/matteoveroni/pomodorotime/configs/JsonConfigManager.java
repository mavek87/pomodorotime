package com.matteoveroni.pomodorotime.configs;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.matteoveroni.pomodorotime.utils.Constants;
import com.matteoveroni.pomodorotime.utils.FileUtils;
import com.matteoveroni.pomodorotime.singleton.GsonSingleton;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private static final String CONFIG_TEMPLATE_PATH = "/" + CONFIG_FOLDER_NAME + "/" + CONFIG_FILE_TEMPLATE_NAME;

    private final Gson gson = GsonSingleton.INSTANCE.getGson();
    private final Path configFile;
    private final Semaphore configFileSemaphore = new Semaphore(1);

    JsonConfigManager() {
        final Path configFolderPath = Paths.get(System.getProperty("user.home"), Constants.APP_NAME, CONFIG_FOLDER_NAME);
        createConfigFolderIfNeeded(configFolderPath);

        configFile = Paths.get(configFolderPath.toAbsolutePath().toString(), CONFIG_FILE_NAME);
        if (Files.exists(configFile)) {
            try {
                final String content = Files.readString(configFile, StandardCharsets.UTF_8);
                final boolean isConfigContentValid = checkIfConfigFileContentIsValid(content);
                if (!isConfigContentValid) {
                    writeDefaultJsonToConfigFile();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try {
                Files.createFile(configFile);
                writeDefaultJsonToConfigFile();
            } catch (IOException ex) {
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

    private void createConfigFolderIfNeeded(Path configFolderPath) {
        try {
            if (!Files.exists(configFolderPath)) {
                Files.createDirectories(configFolderPath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error: impossible to create config folder", ex);
        }
    }

    private void writeDefaultJsonToConfigFile() {
        try {
            final String jsonConfigTemplate = FileUtils.readFromInputStream(JsonConfigManager.class.getResourceAsStream(CONFIG_TEMPLATE_PATH));
            LoggerFactory.getLogger(JsonConfigManager.class).debug("read from {} jsonConfigTemplate {}", CONFIG_TEMPLATE_PATH, jsonConfigTemplate);
            Files.writeString(configFile, jsonConfigTemplate, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ex) {
            throw new RuntimeException("Error: impossible to write default json to config file using the configTemplate", ex);
        }
    }

    private boolean checkIfConfigFileContentIsValid(String content) throws IOException {
        if (content == null || content.isBlank()) {
            LoggerFactory.getLogger(JsonConfigManager.class).error("Error: config file content is null or empty");
            return false;
        }
        try {
            gson.fromJson(content, Config.class);
        } catch (JsonSyntaxException ex) {
            LoggerFactory.getLogger(JsonConfigManager.class).error("Error: config file content is not a valid json");
            return false;
        }
        return true;
    }
}