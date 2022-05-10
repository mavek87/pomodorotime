package com.matteoveroni.pomodorotime.configs;

import com.matteoveroni.pomodorotime.singleton.ExternalFolderPathSingleton;
import lombok.extern.slf4j.Slf4j;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public enum PropertiesConfigManager implements ConfigManager {
    INSTANCE;

    private static final Path CONFIG_PATH = ExternalFolderPathSingleton.INSTANCE.getPath("config", "config.properties");
    private static final String VERSION_PROPS = "/version.properties";

    @Override
    public Config readConfig() {
        final Config config = new Config();
        try (InputStream inputStream = new FileInputStream(CONFIG_PATH.toString())) {
            final Properties props = new Properties();
            props.load(inputStream);
            config.setAppName(props.getProperty("app_name"));
            config.setLanguage(props.getProperty("language"));
            config.setWindowWidth(Double.parseDouble(props.getProperty("window_width")));
            config.setWindowHeight(Double.parseDouble(props.getProperty("window_height")));
            config.setPomodoroDuration(Double.parseDouble(props.getProperty("pomodoro_duration_in_min")));
            config.setPomodoroPauseDuration(Double.parseDouble(props.getProperty("pomodoro_pause_duration_in_min")));
            config.setPomodoroLongPauseDuration(Double.parseDouble(props.getProperty("pomodoro_long_pause_duration_in_min")));
            config.setNumberOfSessionBeforeLongPause(Integer.parseInt(props.getProperty("number_of_sessions_before_long_pause")));
            config.setPlayAlarmSoundOnCompletion(Boolean.parseBoolean(props.getProperty("play_alarm_sound_on_completion")));
            config.setAllowInterruptPomodoro(Boolean.parseBoolean(props.getProperty("allow_interrupt_pomodoro")));
            config.setAllowAbortPomodoro(Boolean.parseBoolean(props.getProperty("allow_abort_pomodoro")));
            config.setPomodoroLoop(Boolean.parseBoolean(props.getProperty("is_pomodoro_loop")));
            config.setPomodoroPauseAlertFullscreen(Boolean.parseBoolean(props.getProperty("is_pomodoro_pause_alert_fullscreen")));
            config.setAllowInterruptPause(Boolean.parseBoolean(props.getProperty("allow_interrupt_pause")));
            config.setAllowAbortPause(Boolean.parseBoolean(props.getProperty("allow_abort_pause")));
        } catch (Exception ex) {
            throw new RuntimeException("Error", ex);
        }
        return config;
    }

    @Override
    public void writeConfig(Config config) {
        try (OutputStream outputStream = new FileOutputStream(CONFIG_PATH.toString())) {
            final Properties props = new Properties();
            props.setProperty("app_name", config.getAppName());
            props.setProperty("language", config.getLanguage());
            props.setProperty("window_width", "" + config.getWindowWidth());
            props.setProperty("window_height", "" + config.getWindowHeight());
            props.setProperty("pomodoro_duration_in_min", "" + config.getPomodoroDuration());
            props.setProperty("pomodoro_pause_duration_in_min", "" + config.getPomodoroPauseDuration());
            props.setProperty("pomodoro_long_pause_duration_in_min", "" + config.getPomodoroLongPauseDuration());
            props.setProperty("number_of_sessions_before_long_pause", "" + config.getNumberOfSessionBeforeLongPause());
            props.setProperty("play_alarm_sound_on_completion", "" + config.isPlayAlarmSoundOnCompletion());
            props.setProperty("allow_interrupt_pomodoro", "" + config.isAllowInterruptPomodoro());
            props.setProperty("allow_abort_pomodoro", "" + config.isAllowAbortPomodoro());
            props.setProperty("is_pomodoro_loop", "" + config.isPomodoroLoop());
            props.setProperty("is_pomodoro_pause_alert_fullscreen", "" + config.isPomodoroPauseAlertFullscreen());
            props.setProperty("allow_interrupt_pause", "" + config.isAllowInterruptPause());
            props.setProperty("allow_abort_pause", "" + config.isAllowAbortPause());
            props.store(outputStream, null);
        } catch (Exception ex) {
            throw new RuntimeException("Error", ex);
        }
    }
//    public String readVersion() throws IOException {
//        final Properties props = readPropertiesFile(PropertiesConfigManager.class.getResource(VERSION_PROPS).getFile());
//        return props.getProperty("version", null);
//    }
//
//    private Properties readPropertiesFile(String propsFile) throws IOException {
//        try (InputStream inputStream = new FileInputStream(propsFile)) {
//            final Properties props = new Properties();
//            props.load(inputStream);
//            return props;
//        }
//    }
}
