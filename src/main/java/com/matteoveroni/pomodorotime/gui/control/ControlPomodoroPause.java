package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import com.matteoveroni.pomodorotime.Settings;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.utils.DurationFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

import static com.matteoveroni.pomodorotime.utils.Constants.*;

@Slf4j
public class ControlPomodoroPause extends BorderPane implements Initializable, LoadableControl {

    private static final String POMODORO_PAUSE = "POMODORO_PAUSE";
    private static final String PAUSE_MIN = "PAUSE_MIN";
    private static final String REMAINING_TIME = "REMAINING_TIME";
    private static final String ELAPSED_TIME = "ELAPSED_TIME";
    private static final String RESUME_PAUSE = "RESUME_PAUSE";
    private static final String INTERRUPT_PAUSE = "INTERRUPT_PAUSE";
    private static final String ABORT_PAUSE = "ABORT_PAUSE";

    @FXML private BorderPane root_border_pane;

    private final Alert parentAlert;
    private final ConfigManager configManager;
    private final Settings settings;
    private final ResourceBundleService resourceBundleService;
    private final FXLocalizationService localizationService;
    private final StringProperty elapsedTimeStringProperty = new SimpleStringProperty("0");
    private final StringProperty remainingTimeStringProperty = new SimpleStringProperty("0");
    private final BooleanProperty isPauseInterruptedProperty = new SimpleBooleanProperty(false);
    private final double pauseDuration;

    private ChangeListener<Duration> durationTimeChangeListener;
    private Timeline timeline;
    private Button btnResume;
    private Button btnPause;
    private Button btnStop;

    // TODO: remove config after modifying settings accordingly
    public ControlPomodoroPause(Alert parentAlert, double pauseDuration, ResourcesService resourcesService, ConfigManager configManager, Settings settings, ResourceBundleService resourceBundleService, FXLocalizationService localizationService) {
        this.parentAlert = parentAlert;
        this.pauseDuration = pauseDuration;
        this.configManager = configManager;
        this.settings = settings;
        this.resourceBundleService = resourceBundleService;
        this.localizationService = localizationService;
        loadControl(resourcesService, Control.POMODORO_PAUSE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug("INITIALIZE " + getClass().getSimpleName());

        final Config currentConfig = configManager.readConfig();

        durationTimeChangeListener = (observable, oldDuration, currentDuration) -> {
            elapsedTimeStringProperty.set(DurationFormatter.formatElapsedDurationTime(currentDuration));
            remainingTimeStringProperty.set(DurationFormatter.formatRemainingDurationTime(pauseDuration, currentDuration));
        };

        final HBox paneActions = new HBox();
        paneActions.setMinHeight(50);
        paneActions.setAlignment(Pos.CENTER);
        paneActions.setSpacing(50);

        buildButtons();

        final ObservableList<Node> paneActionsChildren = paneActions.getChildren();
        paneActionsChildren.clear();
        if (settings.isAllowInterruptPause()) {
            paneActions.getChildren().add(btnPause);
        }
        if (settings.isAllowAbortPause()) {
            paneActionsChildren.add(btnStop);
        }
        settings.allowInterruptPauseProperty().addListener((observableValue, oldValue, newValue) -> {
            log.debug("allowInterruptPauseProperty: {}", newValue);
            if (newValue) {
                paneActionsChildren.add(btnPause);
            } else {
                paneActionsChildren.remove(btnPause);
            }
        });
        settings.allowAbortPauseProperty().addListener((observableValue, oldValue, newValue) -> {
            log.debug("allowAbortPauseProperty: {}", newValue);
            if (newValue) {
                paneActionsChildren.add(btnStop);
            } else {
                paneActionsChildren.remove(btnStop);
            }
        });
        isPauseInterruptedProperty.addListener((observableValue, oldValue, newValue) -> {
            log.debug("isPauseInterruptedProperty: {}", newValue);
            if (newValue) {
                paneActionsChildren.clear();
                paneActionsChildren.add(btnResume);
                if(settings.isAllowInterruptPause()) {
                    paneActionsChildren.add(btnPause);
                }
                if(settings.isAllowAbortPause()) {
                    paneActionsChildren.add(btnStop);
                }
            } else {
                paneActionsChildren.remove(btnResume);
            }
        });

        root_border_pane.setPrefWidth(currentConfig.getWindowWidth() + 50);
        root_border_pane.setPrefHeight(currentConfig.getWindowHeight());
        root_border_pane.setCenter(new FormRenderer(buildFormElapsedTime()));
        root_border_pane.setBottom(paneActions);

        startPause();
    }

    private void startPause() {
        if (isPauseInterruptedProperty.get()) {
            isPauseInterruptedProperty.set(false);
        } else {
            createStartPauseTimeline();
        }
        timeline.play();
    }

    private void interruptPause() {
        isPauseInterruptedProperty.set(true);
        timeline.pause();
    }

    private void abortPause() {
        isPauseInterruptedProperty.set(false);
        unbindButtons();
        timeline.currentTimeProperty().removeListener(durationTimeChangeListener);
        parentAlert.setOnCloseRequest(event -> parentAlert.close());
        parentAlert.close();
    }

    private void createStartPauseTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.minutes(pauseDuration), onCompletionEvent -> abortPause())
        );
        timeline.currentTimeProperty().addListener(durationTimeChangeListener);
    }

    private Form buildFormElapsedTime() {
        final DoubleField fieldPomodoroSession = Field.ofDoubleType(pauseDuration)
                .editable(false)
                .label(PAUSE_MIN);
        final StringField fieldElapsedTime = Field.ofStringType("0")
                .bind(elapsedTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label(ELAPSED_TIME);
        final StringField fieldRemainingTime = Field.ofStringType(remainingTimeStringProperty.get())
                .bind(remainingTimeStringProperty)
                .editable(false)
                .span(ColSpan.HALF)
                .label(REMAINING_TIME);
        return Form.of(
                Section.of(fieldPomodoroSession, fieldElapsedTime, fieldRemainingTime)
                        .title(POMODORO_PAUSE)
                        .collapsible(false)
        ).i18n(resourceBundleService);
    }

    private void buildButtons() {
        btnResume = buildButton(ICON_CODE_PLAY, RESUME_PAUSE, "startPause");
        btnPause = buildButton(ICON_CODE_PAUSE, INTERRUPT_PAUSE, "interruptPause");
        btnStop = buildButton(ICON_CODE_STOP, ABORT_PAUSE, "abortPause");
        bindButtons();
    }

    private Button buildButton(String iconCode, String localizationKey, String methodNameToCallOnAction) {
        final FontIcon iconButton = new FontIcon(iconCode);
        iconButton.setIconSize(BTN_ICON_SIZE);
        iconButton.setIconColor(Paint.valueOf(BTN_ICON_COLOR));
        final Button button = new Button("", iconButton);
        button.setMinSize(BTN_WIDTH, BTN_HEIGHT);
        final Tooltip buttonTooltip = new Tooltip();
        button.setTooltip(buttonTooltip);
        button.setFocusTraversable(false);
        try {
            Class<?>[] noparams = {};
            Method method = ControlPomodoroPause.class.getDeclaredMethod(methodNameToCallOnAction, noparams);
            button.setOnAction(event -> {
                try {
                    method.invoke(this);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        buttonTooltip.textProperty().bind(localizationService.getLocalizedString(localizationKey));
        return button;
    }

    private void bindButtons() {
        btnResume.disableProperty().bind(Bindings.not(isPauseInterruptedProperty));
        btnPause.disableProperty().bind(isPauseInterruptedProperty);
    }

    private void unbindButtons() {
        btnResume.disableProperty().unbind();
        btnPause.disableProperty().unbind();
    }
}