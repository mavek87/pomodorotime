package com.matteoveroni.pomodorotime.gui.control;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Slf4j
public class ControlSettings extends BorderPane implements Initializable, LoadableControl {

    private final ConfigManager configManager;

    private final IntegerProperty pomodoroDurationProperty = new SimpleIntegerProperty(30);
    private final IntegerProperty pomodoroPauseProperty = new SimpleIntegerProperty(5);
    private final IntegerProperty pomodoroLongPauseProperty = new SimpleIntegerProperty(15);
    private final IntegerProperty numberOfSessionsBeforeLongPauseProperty = new SimpleIntegerProperty(4);

    // Boolean
    private final BooleanProperty nightMode = new SimpleBooleanProperty(true);

    // Combobox, Single Selection, with ObservableList
    private final ObservableList resolutionItems = FXCollections.observableArrayList(Arrays.asList(
            "1024x768", "1280x1024", "1440x900", "1920x1080")
    );
    private final ObjectProperty resolutionSelection = new SimpleObjectProperty<>("1024x768");

    // Color
    private final ObjectProperty colorProperty = new SimpleObjectProperty<>(Color.PAPAYAWHIP);
    // Integer Range
    private final IntegerProperty fontSize = new SimpleIntegerProperty(12);

    private final Group timerGroup = Group.of("Timer",
            Setting.of("Pomodoro duration (minutes)", pomodoroDurationProperty, 1, 60),
            Setting.of("Pause duration (minutes)", pomodoroPauseProperty, 1, 60),
            Setting.of("Long pause duration (minutes)", pomodoroLongPauseProperty, 1, 60),
            Setting.of("Sessions before a long pause", numberOfSessionsBeforeLongPauseProperty)
    );

    private final Group screenResolutionGroup = Group.of("Screen resolution", Setting.of("Resolution", resolutionItems, resolutionSelection));
    private final Group screenResolutionSubGroup = Group.of("Screen resolution", Setting.of("Resolution", resolutionItems, resolutionSelection));

    private final Group themesGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
    private final Group themesSubGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));

    private final Group textGroup = Group.of("Text", Setting.of("Font Color", colorProperty));
    private final Group textSubGroup = Group.of("Text", Setting.of("Font Size", fontSize, 6, 36));

    private final PreferencesFx preferencesFx = PreferencesFx.of(ControlSettings.class,
                    Category.of("General", timerGroup).expand(),
                    Category.of("Graphics", screenResolutionGroup, themesGroup, textGroup)
                            .subCategories(
                                    Category.of("Screen", screenResolutionSubGroup),
                                    Category.of("Look and feel", themesSubGroup, textSubGroup)
                            )
            )
            .persistWindowState(false)
            .saveSettings(false)
            .debugHistoryMode(false)
            .instantPersistent(true)
            .buttonsVisibility(true);
//            .dialogTitle("AAAAA");
//            .dialogIcon(resourc);

    public ControlSettings(ResourcesService resourcesService, ConfigManager configManager) {
        this.configManager = configManager;
        loadControl(resourcesService, Control.SETTINGS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
        setCenter(preferencesFx.getView());

        final Config startupConfig = configManager.readConfig();

        pomodoroDurationProperty.set(startupConfig.getPomodoroDuration());
        pomodoroDurationProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroDurationProperty: {}", newValue);
            Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        pomodoroPauseProperty.set(startupConfig.getPomodoroPauseDuration());
        pomodoroPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroPauseProperty: {}", newValue);
            Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroPauseDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        pomodoroLongPauseProperty.set(startupConfig.getPomodoroLongPauseDuration());
        pomodoroLongPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("pomodoroLongPauseProperty: {}", newValue);
            Config currentConfig = configManager.readConfig();
            currentConfig.setPomodoroLongPauseDuration(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });

        numberOfSessionsBeforeLongPauseProperty.set(startupConfig.getNumberOfSessionBeforePause());
        numberOfSessionsBeforeLongPauseProperty.addListener((observable, oldValue, newValue) -> {
            log.debug("numberOfSessionsBeforeLongPauseProperty: {}", newValue);
            Config currentConfig = configManager.readConfig();
            currentConfig.setNumberOfSessionBeforeLongPause(newValue.intValue());
            configManager.writeConfig(currentConfig);
        });
    }

}