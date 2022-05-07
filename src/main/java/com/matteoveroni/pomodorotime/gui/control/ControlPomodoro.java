package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.model.PomodoroModel;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.utils.DurationFormatter;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
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
public class ControlPomodoro extends BorderPane implements Initializable, LoadableControl {

    @FXML private ProgressIndicator progressIndicator;
    @FXML private BorderPane paneFormPomodoro;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    private final Stage stage;
    private final ResourcesService resourcesService;
    private final ConfigManager configManager;
    private final MediaPlayer mediaPlayer;
    private PomodoroModel pomodoroModel;
    private Timeline timeline;
    private Config currentConfig;
    private final StringProperty elapsedTimeStringProperty = new SimpleStringProperty("1");
    private final StringProperty remainingTimeStringProperty = new SimpleStringProperty("1");
    private final ChangeListener<Duration> durationTimeChangeListener = (observable, oldDuration, currentDuration) -> {
        elapsedTimeStringProperty.set(DurationFormatter.formatElapsedDurationTime(currentDuration));
        remainingTimeStringProperty.set(DurationFormatter.formatRemainingDurationTime(currentConfig.getPomodoroDuration(), currentDuration));
    };

    public ControlPomodoro(Stage stage, PomodoroModel pomodoroModel, ResourcesService resourcesService, ConfigManager configManager) {
        this.stage = stage;
        this.pomodoroModel = pomodoroModel;
        this.resourcesService = resourcesService;
        this.configManager = configManager;
        mediaPlayer = new MediaPlayer(new Media(resourcesService.getAlarmAudioURL().toString()));
        loadControl(resourcesService, Control.POMODORO);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
        btnStart.setTooltip(new Tooltip("Start the pomodoro timer"));
        btnStart.setFocusTraversable(false);
        btnStop.setTooltip(new Tooltip("Stop the pomodoro timer"));
        btnStop.setFocusTraversable(false);
        progressIndicator.setMaxSize(640, 480);
        progressIndicator.setVisible(false);
        paneFormPomodoro.setCenter(new FormRenderer(buildFormPomodoro()));
        bindGraphicsToModel();
    }

    @FXML
    void onStartAction(ActionEvent event) {
        startPomodoro();
    }

    @FXML
    void onStopAction(ActionEvent event) {
        stopPomodoro();
    }

    private void startPomodoro() {
        progressIndicator.setVisible(true);

        if (pomodoroModel.isPomodoroCompleted()) {
            rebuildPomodoro();
        }

        final double pomodoroPauseDuration = pomodoroModel.start();
        log.debug("pomodoro pause duration: " + pomodoroPauseDuration);

        currentConfig = configManager.readConfig();
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressIndicator.progressProperty(), 0)),
                new KeyFrame(Duration.minutes(currentConfig.getPomodoroDuration()), onCompletionEvent -> Platform.runLater(() -> {
                    mediaPlayer.play();

                    stopPomodoro();
                    progressIndicator.setVisible(true);

                    stage.setFullScreen(true);
                    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

                    final Alert alert = new Alert(Alert.AlertType.WARNING);
                    final DialogPane dialogPane = alert.getDialogPane();
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle("Pomodoro");
                    alert.setHeaderText("Pomodoro pause");
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(stage);
                    alert.setOnCloseRequest(Event::consume);
                    dialogPane.setContent(new ControlPomodoroPause(alert, pomodoroPauseDuration, resourcesService, configManager));
                    dialogPane.setMinHeight(Region.USE_PREF_SIZE);
                    dialogPane.getButtonTypes().clear();
                    dialogPane.getButtonTypes().add(ButtonType.OK);
                    dialogPane.lookupButton(ButtonType.OK).setVisible(false);
                    dialogPane.getScene().getWindow().setOnCloseRequest(Event::consume);
                    dialogPane.toFront();

                    FXGraphicsUtils.centeredAlert(alert);
                    alert.showAndWait();

                    Platform.runLater(() -> {
                        stage.setFullScreen(false);
                        stage.setWidth(currentConfig.getWindowWidth());
                        stage.setHeight(currentConfig.getWindowHeight());
                        FXGraphicsUtils.centerStage(stage);
                    });

                    mediaPlayer.stop();

                    if (pomodoroModel.isPomodoroCompleted()) {
                        rebuildPomodoro();
                    } else {
                        startPomodoro();
                    }
                }), new KeyValue(progressIndicator.progressProperty(), 1))
        );
        timeline.currentTimeProperty().addListener(durationTimeChangeListener);
        timeline.play();
    }

    private void stopPomodoro() {
        progressIndicator.setVisible(false);
        if (timeline != null) {
            timeline.stop();
            timeline.currentTimeProperty().removeListener(durationTimeChangeListener);
        }
        if (pomodoroModel.isPomodoroRunning() && !pomodoroModel.isPomodoroCompleted()) {
            pomodoroModel.stop();
        }
    }

    private void rebuildPomodoro() {
        unbindGraphicsToModel();
        pomodoroModel = new PomodoroModel(configManager);
        paneFormPomodoro.setCenter(new FormRenderer(buildFormPomodoro()));
        bindGraphicsToModel();
    }

    private void bindGraphicsToModel() {
        paneFormPomodoro.visibleProperty().bind(pomodoroModel.getIsPomodoroRunningProperty());
        btnStart.disableProperty().bind(pomodoroModel.getIsPomodoroCompletedProperty());
        btnStart.disableProperty().bind(pomodoroModel.getIsPomodoroRunningProperty());
        btnStop.disableProperty().bind(pomodoroModel.getIsPomodoroCompletedProperty());
        btnStop.disableProperty().bind(Bindings.not(pomodoroModel.getIsPomodoroRunningProperty()));
    }

    private void unbindGraphicsToModel() {
        paneFormPomodoro.visibleProperty().unbind();
        btnStart.disableProperty().unbind();
        btnStop.disableProperty().unbind();
    }

    private Form buildFormPomodoro() {
        final IntegerField fieldPomodoroSession = Field.ofIntegerType(0)
                .bind(pomodoroModel.getPomodoroSessionProperty())
                .editable(false)
                .label("Session number");
        final StringField fieldRemainingTime = Field.ofStringType(remainingTimeStringProperty.get())
                .bind(remainingTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label("Remaining time");
        final StringField fieldElapsedTime = Field.ofStringType("0")
                .bind(elapsedTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label("Elapsed time");
        return Form.of(
                Section.of(fieldPomodoroSession, fieldElapsedTime, fieldRemainingTime)
                        .title("Pomodoro")
                        .collapsible(false)
        );
    }
}