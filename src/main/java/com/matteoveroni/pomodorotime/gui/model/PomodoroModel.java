package com.matteoveroni.pomodorotime.gui.model;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public final class PomodoroModel {

    private final ConfigManager configManager;
    private final BooleanProperty isPomodoroRunningProperty = new SimpleBooleanProperty(false);
    private int pomodoroCounter = 0;
    private Double pomodoroPauseDuration = null;

    public PomodoroModel(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public double start() {
        if (isPomodoroRunning())
            throw new IllegalStateException("Pomodoro model state exception: a pomodoro session is already being running, so calling again the start method is wrong");

        pomodoroCounter++;
        pomodoroPauseDuration = calculatePomodoroPauseDuration();
        isPomodoroRunningProperty.set(true);

        return pomodoroPauseDuration;
    }

    public int getPomodoroSession() {
        return pomodoroCounter;
    }

    public BooleanProperty isPomodoroRunningProperty() {
        return isPomodoroRunningProperty;
    }

    public boolean isPomodoroRunning() {
        return isPomodoroRunningProperty.get();
    }

    public void stop() {
        if (!isPomodoroRunning())
            throw new IllegalStateException("Pomodoro model state exception: any pomodoro session started, so calling the stop method is wrong");

        pomodoroPauseDuration = null;
        isPomodoroRunningProperty.set(false);
    }

    private double calculatePomodoroPauseDuration() {
        final Config config = configManager.readConfig();
        final int numberOfSessionBeforeLongPause = config.getNumberOfSessionBeforeLongPause();
        if ((pomodoroCounter % (numberOfSessionBeforeLongPause + 1)) == 0) {
            return config.getPomodoroLongPauseDuration();
        } else {
            return config.getPomodoroPauseDuration();
        }
    }
}
