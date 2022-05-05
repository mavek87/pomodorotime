package com.matteoveroni.pomodorotime.configs;

public interface ConfigManager {

    Config readConfig();

    void writeConfig(Config config);
}
