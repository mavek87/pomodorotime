package com.matteoveroni.pomodorotime.gui.model;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class PomodoroModel {

    public static final String ERROR_START_ALREADY_RUNNING_POMODORO = "Pomodoro model state exception: a pomodoro session is running, so calling again the start method is wrong";
    public static final String ERROR_START_ALREADY_COMPLETED_POMODORO = "This pomodoro is already completed, so you can't start it again";
    public static final String ERROR_STOP_NO_RUNNING_POMODORO = "Pomodoro model state exception: any pomodoro session is running, so calling the stop method is wrong";
    public static final String ERROR_STOP_ALREADY_COMPLETED_POMODORO = "This pomodoro is already completed, so you can't stop it again";

    private final ConfigManager configManager;
    private final BooleanProperty isPomodoroRunningProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty isPomodoroCompletedProperty = new SimpleBooleanProperty(false);
    private final IntegerProperty pomodoroCounterProperty = new SimpleIntegerProperty(0);
    private Double pomodoroPauseDuration = null;

    public PomodoroModel(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public double start() {
        if (isPomodoroRunning())
            throw new IllegalStateException(ERROR_START_ALREADY_RUNNING_POMODORO);
        else if (isPomodoroCompleted())
            throw new IllegalStateException(ERROR_START_ALREADY_COMPLETED_POMODORO);

        pomodoroCounterProperty.set(pomodoroCounterProperty.get() + 1);
        pomodoroPauseDuration = calculatePomodoroPauseDuration(configManager.readConfig());
        isPomodoroRunningProperty.set(true);
        return pomodoroPauseDuration;
    }

    public void stop() {
        if (!isPomodoroRunning())
            throw new IllegalStateException(ERROR_STOP_NO_RUNNING_POMODORO);
        else if (isPomodoroCompleted())
            throw new IllegalStateException(ERROR_STOP_ALREADY_COMPLETED_POMODORO);

        pomodoroPauseDuration = null;
        isPomodoroRunningProperty.set(false);
        isPomodoroCompletedProperty.set(calculateIfPomodoroIsCompleted(configManager.readConfig()));
    }

    public int getPomodoroSession() {
        return pomodoroCounterProperty.get();
    }

    public IntegerProperty getPomodoroSessionProperty() {
        return pomodoroCounterProperty;
    }

    public boolean isPomodoroRunning() {
        return isPomodoroRunningProperty.get();
    }

    public BooleanProperty getIsPomodoroRunningProperty() {
        return isPomodoroRunningProperty;
    }

    public boolean isPomodoroCompleted() {
        return isPomodoroCompletedProperty.get();
    }

    public BooleanProperty getIsPomodoroCompletedProperty() {
        return isPomodoroCompletedProperty;
    }

    // TODO: numberOfSessionBeforeLongPause > 0
    private double calculatePomodoroPauseDuration(Config config) {
        final int numberOfSessionBeforeLongPause = config.getNumberOfSessionBeforeLongPause();
        if ((pomodoroCounterProperty.get() % numberOfSessionBeforeLongPause) == 0) {
            return config.getPomodoroLongPauseDuration();
        } else {
            return config.getPomodoroPauseDuration();
        }
    }

    private boolean calculateIfPomodoroIsCompleted(Config config) {
        if (config.isPomodoroLoop()) {
            return false;
        } else {
            return pomodoroCounterProperty.get() >= config.getNumberOfSessionBeforeLongPause();
        }
    }
}
