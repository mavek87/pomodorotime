package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.gui.screen.ScreenResolution;
import com.matteoveroni.pomodorotime.services.localization.SupportedLocale;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class Settings {

    public static final SupportedLocale DEFAULT_LOCALE = SupportedLocale.US;
    public static final ScreenResolution DEFAULT_SCREEN_SIZE_RESOLUTION = ScreenResolution.RESOLUTION_1024x768;

    private final DoubleProperty pomodoroDuration = new SimpleDoubleProperty(30);
    private final DoubleProperty pomodoroPause = new SimpleDoubleProperty(5);
    private final DoubleProperty pomodoroLongPause = new SimpleDoubleProperty(15);
    private final IntegerProperty numberOfSessionsBeforeLongPause = new SimpleIntegerProperty(4);
    private final BooleanProperty playAlarmSoundOnCompletion = new SimpleBooleanProperty(false);
    private final BooleanProperty isPomodoroLoop = new SimpleBooleanProperty(true);
    private final BooleanProperty allowInterruptPomodoro = new SimpleBooleanProperty(false);
    private final BooleanProperty allowAbortPomodoro = new SimpleBooleanProperty(false);
    private final BooleanProperty isFullScreenPauseAlertOn = new SimpleBooleanProperty(false);
    private final BooleanProperty allowInterruptPause = new SimpleBooleanProperty(false);
    private final BooleanProperty allowAbortPause = new SimpleBooleanProperty(false);
    private final ObservableList<SupportedLocale> selectedLocale = FXCollections.observableArrayList(Arrays.asList(SupportedLocale.values()));
    private final ObjectProperty<SupportedLocale> selectedLocaleProperty = new SimpleObjectProperty<>(DEFAULT_LOCALE);
    private final ObservableList<ScreenResolution> resolutionItems = FXCollections.observableArrayList(Arrays.asList(ScreenResolution.values()));
    private final ObjectProperty<ScreenResolution> resolutionSelectionProperty = new SimpleObjectProperty<>(DEFAULT_SCREEN_SIZE_RESOLUTION);

    public double getPomodoroDuration() {
        return pomodoroDuration.get();
    }

    public DoubleProperty pomodoroDurationProperty() {
        return pomodoroDuration;
    }

    public void setPomodoroDuration(double pomodoroDuration) {
        this.pomodoroDuration.set(pomodoroDuration);
    }

    public double getPomodoroPause() {
        return pomodoroPause.get();
    }

    public DoubleProperty pomodoroPauseProperty() {
        return pomodoroPause;
    }

    public void setPomodoroPause(double pomodoroPause) {
        this.pomodoroPause.set(pomodoroPause);
    }

    public double getPomodoroLongPause() {
        return pomodoroLongPause.get();
    }

    public DoubleProperty pomodoroLongPauseProperty() {
        return pomodoroLongPause;
    }

    public void setPomodoroLongPause(double pomodoroLongPause) {
        this.pomodoroLongPause.set(pomodoroLongPause);
    }

    public int getNumberOfSessionsBeforeLongPause() {
        return numberOfSessionsBeforeLongPause.get();
    }

    public IntegerProperty numberOfSessionsBeforeLongPauseProperty() {
        return numberOfSessionsBeforeLongPause;
    }

    public void setNumberOfSessionsBeforeLongPause(int numberOfSessionsBeforeLongPause) {
        this.numberOfSessionsBeforeLongPause.set(numberOfSessionsBeforeLongPause);
    }

    public boolean getPlayAlarmSoundOnCompletion() {
        return playAlarmSoundOnCompletion.get();
    }

    public BooleanProperty playAlarmSoundOnCompletionProperty(){
        return playAlarmSoundOnCompletion;
    }

    public void setPlayAlarmSoundOnCompletion(boolean playAlarmSoundOnCompletion) {
        this.playAlarmSoundOnCompletion.set(playAlarmSoundOnCompletion);
    }

    public boolean isIsPomodoroLoop() {
        return isPomodoroLoop.get();
    }

    public BooleanProperty isPomodoroLoopProperty() {
        return isPomodoroLoop;
    }

    public void setIsPomodoroLoop(boolean isPomodoroLoop) {
        this.isPomodoroLoop.set(isPomodoroLoop);
    }

    public boolean isAllowInterruptPomodoro() {
        return allowInterruptPomodoro.get();
    }

    public BooleanProperty allowInterruptPomodoroProperty() {
        return allowInterruptPomodoro;
    }

    public void setAllowInterruptPomodoro(boolean allowInterruptPomodoro) {
        this.allowInterruptPomodoro.set(allowInterruptPomodoro);
    }

    public boolean isAllowAbortPomodoro() {
        return allowAbortPomodoro.get();
    }

    public BooleanProperty allowAbortPomodoroProperty() {
        return allowAbortPomodoro;
    }

    public void setAllowAbortPomodoro(boolean allowAbortPomodoro) {
        this.allowAbortPomodoro.set(allowAbortPomodoro);
    }

    public boolean isIsFullScreenPauseAlertOn() {
        return isFullScreenPauseAlertOn.get();
    }

    public BooleanProperty isFullScreenPauseAlertOnProperty() {
        return isFullScreenPauseAlertOn;
    }

    public void setIsFullScreenPauseAlertOn(boolean isFullScreenPauseAlertOn) {
        this.isFullScreenPauseAlertOn.set(isFullScreenPauseAlertOn);
    }

    public boolean isAllowInterruptPause() {
        return allowInterruptPause.get();
    }

    public BooleanProperty allowInterruptPauseProperty() {
        return allowInterruptPause;
    }

    public void setAllowInterruptPause(boolean allowInterruptPause) {
        this.allowInterruptPause.set(allowInterruptPause);
    }

    public boolean isAllowAbortPause() {
        return allowAbortPause.get();
    }

    public BooleanProperty allowAbortPauseProperty() {
        return allowAbortPause;
    }

    public void setAllowAbortPause(boolean allowAbortPause) {
        this.allowAbortPause.set(allowAbortPause);
    }

    public ObservableList<SupportedLocale> getSelectedLocale() {
        return selectedLocale;
    }

    public ObjectProperty<SupportedLocale> selectedLocaleProperty() {
        return selectedLocaleProperty;
    }

    public void setSelectedLocale(SupportedLocale locale) {
        selectedLocaleProperty.set(locale);
    }

    public ObservableList<ScreenResolution> getResolutionItems() {
        return resolutionItems;
    }

    public ScreenResolution getResolutionSelection() {
        return resolutionSelectionProperty.get();
    }

    public ObjectProperty<ScreenResolution> resolutionSelectionProperty() {
        return resolutionSelectionProperty;
    }

    public void setResolutionSelection(ScreenResolution resolutionSelection) {
        resolutionSelectionProperty.set(resolutionSelection);
    }
}
