package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
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
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ControlPomodoro extends BorderPane implements Initializable, LoadableControl {

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
    private final Stage stage;

    private Timeline timeline;

    public ControlPomodoro(Stage stage, ResourcesService resourcesService) {
        this.stage = stage;
        Media alarmSound = new Media(resourcesService.getAlarmAudioURL().toString());
        mediaPlayer = new MediaPlayer(alarmSound);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ONE);
            mediaPlayer.play();
        });
        loadControl(resourcesService, Control.POMODORO);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
        btnStart.setTooltip(new Tooltip("Start the pomodoro timer"));
        btnStart.setFocusTraversable(false);
        btnStop.setTooltip(new Tooltip("Stop the pomodoro timer"));
        btnStop.setFocusTraversable(false);
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
    }

    private void startAlertTimer() {
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressIndicator.progressProperty(), 0)),
                new KeyFrame(Duration.minutes(fieldAlarmTimeMinutes.getValue()), onCompletionEvent -> {
                    mediaPlayer.play();
                    Platform.runLater(() -> {
                        Alert alertDialog = new Alert(Alert.AlertType.WARNING);
                        alertDialog.setTitle("Alert");
                        alertDialog.setHeaderText("Alert fired");
                        alertDialog.setContentText("It's time to stop!");
                        alertDialog.initModality(Modality.APPLICATION_MODAL);
                        alertDialog.initOwner(stage);
                        alertDialog.showAndWait();
                        stopAlert();
                    });
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
        progressIndicator.setProgress(0);
        mediaPlayer.stop();
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