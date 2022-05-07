package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.utils.DurationFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class ControlPomodoroPause extends BorderPane implements Initializable, LoadableControl {

    @FXML private BorderPane root_border_pane;

    private final Alert parentAlert;
    private final ConfigManager configManager;
    private final StringProperty elapsedTimeStringProperty = new SimpleStringProperty("0");
    private final StringProperty remainingTimeStringProperty = new SimpleStringProperty("0");
    private final double pauseDuration;

    private ChangeListener<Duration> durationTimeChangeListener;
    private Timeline timeline;

    public ControlPomodoroPause(Alert parentAlert, double pauseDuration, ResourcesService resourcesService, ConfigManager configManager) {
        this.parentAlert = parentAlert;
        this.pauseDuration = pauseDuration;
        this.configManager = configManager;
        loadControl(resourcesService, Control.POMODORO_PAUSE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug("INITIALIZE " + getClass().getSimpleName());

        Config currentConfig = configManager.readConfig();

        durationTimeChangeListener = (observable, oldDuration, currentDuration) -> {
            elapsedTimeStringProperty.set(DurationFormatter.formatElapsedDurationTime(currentDuration));
            remainingTimeStringProperty.set(DurationFormatter.formatRemainingDurationTime(pauseDuration, currentDuration));
        };

        root_border_pane.setPrefWidth(currentConfig.getWindowWidth() + 50);
        root_border_pane.setPrefHeight(currentConfig.getWindowHeight());

        root_border_pane.setCenter(new FormRenderer(buildFormElapsedTime()));

        startAlertTimer();
    }

    private void startAlertTimer() {
        timeline = new Timeline(
                new KeyFrame(Duration.minutes(pauseDuration), onCompletionEvent -> {
//                    Button okButton = (Button) parentAlert.getDialogPane().lookupButton(ButtonType.OK);
                    timeline.currentTimeProperty().removeListener(durationTimeChangeListener);
                    parentAlert.setOnCloseRequest(event -> parentAlert.close());
                    parentAlert.close();
                })
        );
        timeline.currentTimeProperty().addListener(durationTimeChangeListener);
        timeline.play();
    }

    private Form buildFormElapsedTime() {
        final DoubleField fieldPomodoroSession = Field.ofDoubleType(pauseDuration)
                .editable(false)
                .label("Pause (min)");
        final StringField fieldElapsedTime = Field.ofStringType("0")
                .bind(elapsedTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label("Elapsed time");
        final StringField fieldRemainingTime = Field.ofStringType(remainingTimeStringProperty.get())
                .bind(remainingTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label("Remaining time");
        return Form.of(
                Section.of(fieldPomodoroSession, fieldElapsedTime, fieldRemainingTime)
                        .title("Pomodoro pause")
                        .collapsible(false)
        );
    }
}