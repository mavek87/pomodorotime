package com.matteoveroni.pomodorotime.controllers;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class PomodoroController implements Initializable {

    @FXML private ProgressIndicator progressIndicator;
    @FXML private BorderPane paneFormAlertSettings;
    @FXML private BorderPane paneFormAlertTimer;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    private final BooleanProperty isAlertTimerStartedBooleanProperty = new SimpleBooleanProperty(false);
    private final StringProperty elapsedTimeStringProperty = new SimpleStringProperty("1");
    private final StringProperty remainingTimeStringProperty = new SimpleStringProperty("1");
    private final ChangeListener<Duration> durationTimeChangeListener = (observable, oldDuration, currentDuration) -> {
        elapsedTimeStringProperty.set(formatElapsedDurationTime(currentDuration));
        remainingTimeStringProperty.set(formatRemainingDurationTime(currentDuration));
    };
    private final DoubleField fieldAlarmTimeMinutes = Field.ofDoubleType(1)
            .required(true)
            .validate(CustomValidator.forPredicate(doubleValue -> doubleValue > 0, "The amount of minutes must be more than 0"))
            .placeholder("Insert an amount of time in minutes")
            .span(ColSpan.HALF)
            .label("Alarm time (minutes)");
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
    private final MediaPlayer mediaPlayer;

    private Timeline timeline;

    public PomodoroController() {
        Media alarmSound = new Media(getClass().getClassLoader().getResource("alarm.mp3").toString());
        mediaPlayer = new MediaPlayer(alarmSound);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ONE);
            mediaPlayer.play();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldRemainingTime.setBindingMode(BindingMode.CONTINUOUS);
        fieldElapsedTime.setBindingMode(BindingMode.CONTINUOUS);
        progressIndicator.setMaxSize(640, 480);
        progressIndicator.visibleProperty().bind(isAlertTimerStartedBooleanProperty);
        paneFormAlertSettings.setCenter(new FormRenderer(buildFormAlarmSettings()));
        paneFormAlertSettings.visibleProperty().bind(Bindings.not(isAlertTimerStartedBooleanProperty));
        paneFormAlertTimer.setCenter(new FormRenderer(buildFormElapsedTime()));
        paneFormAlertTimer.visibleProperty().bind(isAlertTimerStartedBooleanProperty);
        btnStart.disableProperty().bind(isAlertTimerStartedBooleanProperty);
        btnStop.disableProperty().bind(Bindings.not(isAlertTimerStartedBooleanProperty));
    }

    @FXML
    void onStartAction(ActionEvent event) {
        if (fieldAlarmTimeMinutes.isValid()) {
            fieldAlarmTimeMinutes.persist();
            startAlertTimer();
            fieldAlarmTimeMinutes.editable(false);
        }
    }

    @FXML
    void onStopAction(ActionEvent event) {
        stopAlert();
        progressIndicator.setProgress(0);
        mediaPlayer.stop();
    }

    private void startAlertTimer() {
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressIndicator.progressProperty(), 0)),
                new KeyFrame(Duration.minutes(fieldAlarmTimeMinutes.getValue()), onCompletionEvent -> {
                    mediaPlayer.play();
                }, new KeyValue(progressIndicator.progressProperty(), 1))
        );
        timeline.currentTimeProperty().addListener(durationTimeChangeListener);
        timeline.play();
        isAlertTimerStartedBooleanProperty.setValue(true);
    }

    private void stopAlert() {
        if (timeline != null) {
            timeline.stop();
            timeline.currentTimeProperty().removeListener(durationTimeChangeListener);
        }
        isAlertTimerStartedBooleanProperty.setValue(false);
        fieldAlarmTimeMinutes.editable(true);
        fieldAlarmTimeMinutes.reset();
    }

    private Form buildFormAlarmSettings() {
        return Form.of(
                Section.of(fieldAlarmTimeMinutes)
                        .title("Alarm settings")
                        .collapsible(false)
        );
    }

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
        return convertSecondsToHHMMSS((long) ((fieldAlarmTimeMinutes.getValue() * 60) - duration.toSeconds()));
    }

    private String convertSecondsToHHMMSS(long seconds) {
        final long HH = TimeUnit.SECONDS.toHours(seconds);
        final long MM = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        final long SS = TimeUnit.SECONDS.toSeconds(seconds) % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}