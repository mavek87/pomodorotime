package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import com.matteoveroni.pomodorotime.Settings;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.gui.model.PomodoroModel;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.utils.DurationFormatter;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
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
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
import static com.matteoveroni.pomodorotime.utils.Constants.LocalizationKeys.*;

@Slf4j
public class ControlPomodoro extends BorderPane implements Initializable, LoadableControl {

    public static final int PROGRESS_INDICATOR_WIDTH = 640;
    public static final int PROGRESS_INDICATOR_HEIGHT = 480;

    @FXML private ProgressIndicator progressIndicator;
    @FXML private BorderPane paneFormPomodoro;
    @FXML private HBox paneActions;
    @FXML private Button btnStart;
    @FXML private Button btnPause;
    @FXML private Button btnStop;

    private final Stage stage;
    private final AppViewController appViewController;
    private final ResourcesService resourcesService;
    private final ConfigManager configManager;
    private final MediaPlayer mediaPlayer;
    private final Settings settings;
    private final FXLocalizationService localizationService;
    private final ResourceBundleService resourceBundleService;
    private final StringProperty elapsedTimeStringProperty = new SimpleStringProperty("0");
    private final StringProperty remainingTimeStringProperty = new SimpleStringProperty("0");
    private final BooleanProperty isPomodoroPausedProperty = new SimpleBooleanProperty(false);

    private ChangeListener<Duration> durationTimeChangeListener;
    private PomodoroModel pomodoroModel;
    private Timeline timeline;
    private Config currentConfig;

    public ControlPomodoro(Stage stage, AppViewController appViewController, PomodoroModel pomodoroModel, ResourcesService resourcesService, ConfigManager configManager, Settings settings, FXLocalizationService localizationService, ResourceBundleService resourceBundleService) {
        this.stage = stage;
        this.appViewController = appViewController;
        this.pomodoroModel = pomodoroModel;
        this.resourcesService = resourcesService;
        this.configManager = configManager;
        this.mediaPlayer = new MediaPlayer(new Media(resourcesService.getAlarmAudioURL().toString()));
        this.settings = settings;
        this.localizationService = localizationService;
        this.resourceBundleService = resourceBundleService;
        loadControl(resourcesService, Control.POMODORO);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug("INITIALIZE " + getClass().getSimpleName());

        durationTimeChangeListener = (observable, oldDuration, currentDuration) -> {
            elapsedTimeStringProperty.set(DurationFormatter.formatElapsedDurationTime(currentDuration));
            remainingTimeStringProperty.set(DurationFormatter.formatRemainingDurationTime(currentConfig.getPomodoroDuration(), currentDuration));
        };

        buildButtons();

        progressIndicator.setMaxSize(PROGRESS_INDICATOR_WIDTH, PROGRESS_INDICATOR_HEIGHT);
        progressIndicator.setVisible(false);
        paneFormPomodoro.setCenter(new FormRenderer(buildFormPomodoro()));

        bindGraphicsToModel();

        final ObservableList<Node> paneActionsChildren = paneActions.getChildren();
        paneActionsChildren.clear();
        paneActionsChildren.add(btnStart);
        if (settings.isAllowInterruptPomodoro()) {
            paneActions.getChildren().add(btnPause);
        }
        if (settings.isAllowAbortPomodoro()) {
            paneActionsChildren.add(btnStop);
        }

        settings.allowInterruptPomodoroProperty().addListener((observableValue, oldValue, newValue) -> {
            log.debug("allowInterruptPomodoroProperty: {}", newValue);
            if (newValue) {
                paneActionsChildren.add(btnPause);
            } else {
                paneActionsChildren.remove(btnPause);
            }
        });
        settings.allowAbortPomodoroProperty().addListener((observableValue, oldValue, newValue) -> {
            log.debug("allowAbortPomodoroProperty: {}", newValue);
            if (newValue) {
                paneActionsChildren.add(btnStop);
            } else {
                paneActionsChildren.remove(btnStop);
            }
        });
    }

    private void startPomodoro() {
        if (isPomodoroPausedProperty.get()) {
            isPomodoroPausedProperty.set(false);
        } else {
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
                        if (settings.getPlayAlarmSoundOnCompletion()) {
                            mediaPlayer.play();
                        }

                        stopPomodoro();
                        progressIndicator.setVisible(true);

                        appViewController.setOverlayPane(true);

                        if (currentConfig.isPomodoroPauseAlertFullscreen()) {
                            stage.setFullScreen(true);
                            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                        }

                        final Alert pomodoroPauseAlert = buildPomodoroPauseAlert(pomodoroPauseDuration);
                        pomodoroPauseAlert.showAndWait();

                        if (settings.getPlayAlarmSoundOnCompletion()) {
                            mediaPlayer.stop();
                        }
                        appViewController.setOverlayPane(false);

                        Platform.runLater(() -> {
                            if (currentConfig.isPomodoroPauseAlertFullscreen()) {
                                stage.setFullScreen(false);
                            }
                            stage.setWidth(currentConfig.getWindowWidth());
                            stage.setHeight(currentConfig.getWindowHeight());
                            FXGraphicsUtils.centerStage(stage);
                        });

                        if (pomodoroModel.isPomodoroCompleted()) {
                            rebuildPomodoro();
                        } else {
                            startPomodoro();
                        }
                    }), new KeyValue(progressIndicator.progressProperty(), 1))
            );
            timeline.currentTimeProperty().addListener(durationTimeChangeListener);
        }
        timeline.play();
    }

    private void pausePomodoro() {
        isPomodoroPausedProperty.set(true);
        timeline.pause();
    }

    private void stopPomodoro() {
        isPomodoroPausedProperty.set(false);
        progressIndicator.setVisible(false);
        timeline.stop();
        timeline.currentTimeProperty().removeListener(durationTimeChangeListener);
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
        btnStart.disableProperty().bind(Bindings.and(pomodoroModel.getIsPomodoroRunningProperty(), isPomodoroPausedProperty.not()));
        btnPause.disableProperty().bind(pomodoroModel.getIsPomodoroCompletedProperty());
        btnPause.disableProperty().bind(Bindings.or(Bindings.not(pomodoroModel.getIsPomodoroRunningProperty()), isPomodoroPausedProperty));
        btnStop.disableProperty().bind(pomodoroModel.getIsPomodoroCompletedProperty());
        btnStop.disableProperty().bind(Bindings.not(pomodoroModel.getIsPomodoroRunningProperty()));
    }

    private void unbindGraphicsToModel() {
        paneFormPomodoro.visibleProperty().unbind();
        btnStart.disableProperty().unbind();
        btnPause.disableProperty().unbind();
        btnStop.disableProperty().unbind();
    }

    private Form buildFormPomodoro() {
        final IntegerField fieldPomodoroSession = Field.ofIntegerType(0)
                .bind(pomodoroModel.getPomodoroSessionProperty())
                .editable(false)
                .label(SESSION_NUMBER);
        final StringField fieldRemainingTime = Field.ofStringType(remainingTimeStringProperty.get())
                .bind(remainingTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label(REMAINING_TIME);
        final StringField fieldElapsedTime = Field.ofStringType("0")
                .bind(elapsedTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label(ELAPSED_TIME);
        return Form.of(
                Section.of(fieldPomodoroSession, fieldElapsedTime, fieldRemainingTime)
                        .title(POMODORO)
                        .collapsible(false)
        ).i18n(resourceBundleService);
    }

    private Alert buildPomodoroPauseAlert(double pomodoroPauseDuration) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        final DialogPane dialogPane = alert.getDialogPane();
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(localizationService.translateLocalizedString(POMODORO));
        alert.setHeaderText(localizationService.translateLocalizedString(POMODORO_PAUSE));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setOnCloseRequest(Event::consume);
        dialogPane.getScene().getWindow().setOnCloseRequest(Event::consume);
        dialogPane.setContent(new ControlPomodoroPause(alert, pomodoroPauseDuration, resourcesService, configManager, settings, resourceBundleService, localizationService));
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.getButtonTypes().clear();
        FXGraphicsUtils.centeredAlert(alert);
        return alert;
    }

    private void buildButtons() {
        final Tooltip btnStartTooltip = new Tooltip();
        btnStart.setTooltip(btnStartTooltip);
        btnStart.setFocusTraversable(false);
        btnStart.setOnAction(event -> startPomodoro());
        final Tooltip btnPauseTooltip = new Tooltip();
        btnPause.setTooltip(btnPauseTooltip);
        btnPause.setFocusTraversable(false);
        btnPause.setOnAction(event -> pausePomodoro());
        final Tooltip btnStopTooltip = new Tooltip();
        btnStop.setTooltip(btnStopTooltip);
        btnStop.setFocusTraversable(false);
        btnStop.setOnAction(event -> stopPomodoro());

        btnStartTooltip.textProperty().bind(localizationService.getLocalizedString(START_POMODORO));
        btnPauseTooltip.textProperty().bind(localizationService.getLocalizedString(PAUSE_POMODORO));
        btnStopTooltip.textProperty().bind(localizationService.getLocalizedString(STOP_POMODORO));
    }
}