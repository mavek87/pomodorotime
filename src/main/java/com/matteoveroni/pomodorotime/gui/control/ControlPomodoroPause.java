package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ControlPomodoroPause extends BorderPane implements Initializable, LoadableControl {

    @FXML private BorderPane root_border_pane;

    //    private final BooleanProperty isAlertTimerStartedBooleanProperty = new SimpleBooleanProperty(false);
    private final StringProperty elapsedTimeStringProperty = new SimpleStringProperty("1");
    private final StringProperty remainingTimeStringProperty = new SimpleStringProperty("1");
    private final ChangeListener<Duration> durationTimeChangeListener = (observable, oldDuration, currentDuration) -> {
        elapsedTimeStringProperty.set(formatElapsedDurationTime(currentDuration));
        remainingTimeStringProperty.set(formatRemainingDurationTime(currentDuration));
    };
    private final StringField fieldRemainingTime = Field.ofStringType(remainingTimeStringProperty.get())
            .bind(remainingTimeStringProperty)
            .editable(false)
            .span(ColSpan.HALF)
            .label("Remaining time");
    private final StringField fieldElapsedTime = Field.ofStringType("0")
            .bind(elapsedTimeStringProperty)
            .editable(false)
            .span(ColSpan.HALF)
            .label("Elapsed time");
    private final Stage stage;
    private final ConfigManager configManager;

    private final int pauseDuration;

    public ControlPomodoroPause(Stage stage, int pauseDuration, ResourcesService resourcesService, ConfigManager configManager) {
        this.stage = stage;
        this.pauseDuration = pauseDuration;
        this.configManager = configManager;
        loadControl(resourcesService, Control.POMODORO_PAUSE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug("INITIALIZE " + getClass().getSimpleName());

        Config initialConfig = configManager.readConfig();

        root_border_pane.setPrefWidth(initialConfig.getWindowWidth() + 50);
        root_border_pane.setPrefHeight(initialConfig.getWindowHeight());

        fieldRemainingTime.setBindingMode(BindingMode.CONTINUOUS);
        fieldElapsedTime.setBindingMode(BindingMode.CONTINUOUS);
        root_border_pane.setCenter(new FormRenderer(buildFormElapsedTime()));

        startAlertTimer();
    }

    private void startAlertTimer() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.minutes(pauseDuration), onCompletionEvent -> {
                    log.info("FINITOOOOO");
                })
        );
        timeline.setCycleCount(2);
        timeline.currentTimeProperty().addListener(durationTimeChangeListener);
        timeline.play();
    }

//    private void stopAlert() {
//        if (timeline != null) {
//            timeline.stop();
//            timeline.currentTimeProperty().removeListener(durationTimeChangeListener);
//        }
//        isAlertTimerStartedBooleanProperty.setValue(false);
//        fieldAlarmTimeMinutes.editable(true);
//        fieldAlarmTimeMinutes.reset();
//        progressIndicator.setProgress(0);
//        mediaPlayer.stop();
//    }

    private Form buildFormElapsedTime() {
        return Form.of(
                Section.of(fieldElapsedTime, fieldRemainingTime)
                        .title("Alarm")
                        .collapsible(false)
        );
    }

    private String formatElapsedDurationTime(Duration duration) {
        return convertSecondsToHHMMSS((long) duration.toSeconds());
    }

    private String formatRemainingDurationTime(Duration duration) {
        return convertSecondsToHHMMSS((long) ((pauseDuration * 60L) - duration.toSeconds()));
    }

    private String convertSecondsToHHMMSS(long seconds) {
        final long HH = TimeUnit.SECONDS.toHours(seconds);
        final long MM = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        final long SS = TimeUnit.SECONDS.toSeconds(seconds) % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}