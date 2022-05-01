package com.matteoveroni.pomodorotime.controllers;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import jakarta.annotation.PreDestroy;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;

@Slf4j
public class SettingsController {

//    private final FXLocalizationService localizationService;

    @FXML private BorderPane root;
    @FXML private AnchorPane navbar_pane;

    private final SimpleStringProperty welcomeProp = new SimpleStringProperty("");
    private final SimpleStringProperty welcomeProp2 = new SimpleStringProperty("");

//    private final List<Locale> locales = Arrays.stream(SupportedLocale.values()).map(SupportedLocale::getLocale).collect(Collectors.toList());
//    private final ListProperty<Locale> localesListProperty = new SimpleListProperty<>(FXCollections.observableArrayList(locales));
//    private final ObjectProperty<Locale> selectedLocalesProperty = new SimpleObjectProperty<>(locales.get(0));

//    @Inject
//    public SettingsController(FXLocalizationService localizationService) {
//        this.localizationService = localizationService;
//    }

    @FXML
    private void initialize() {
        log.info("INITIALIZE " + getClass().getName());

//        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
//
//        localizationService.selectedLocaleProperty().bind(selectedLocalesProperty);
//
//        navbar_pane.getChildren().add(new ControlNavBar(localizationService));

        PreferencesFx preferences = createPreferences();
        root.setCenter(preferences.getView());

        welcomeProp.addListener((observableValue, s, t1) -> System.out.println("1: " + t1));
        welcomeProp2.addListener((observableValue, s, t1) -> System.out.println("2: " + t1));
    }

    @PreDestroy
    private void dispose() {
    }

    // TODO: just a stub method. clean the mess
    private PreferencesFx createPreferences() {

        // Boolean
        BooleanProperty nightMode = new SimpleBooleanProperty(true);

        // Combobox, Single Selection, with ObservableList
        ObservableList resolutionItems = FXCollections.observableArrayList(Arrays.asList(
                "1024x768", "1280x1024", "1440x900", "1920x1080")
        );
        ObjectProperty resolutionSelection = new SimpleObjectProperty<>("1024x768");

        // Color
        ObjectProperty colorProperty = new SimpleObjectProperty<>(Color.PAPAYAWHIP);
        // Integer Range
        IntegerProperty fontSize = new SimpleIntegerProperty(12);

        IntegerProperty pomodoroDurationProperty = new SimpleIntegerProperty(30);
        IntegerProperty pomodoroPauseProperty = new SimpleIntegerProperty(5);
        IntegerProperty pomodoroLongPauseProperty = new SimpleIntegerProperty(15);
        IntegerProperty numberOfSessionsBeforeLongPauseProperty = new SimpleIntegerProperty(4);

        // FileChooser / DirectoryChooser
        ObjectProperty fileProperty = new SimpleObjectProperty<>();
        Setting.of("File", fileProperty, false);     // FileChooser
        Setting.of("Directory", fileProperty, true); // DirectoryChooser


        final Group timerGroup = Group.of("Timer",
                Setting.of("Pomodoro duration (minutes)", pomodoroDurationProperty, 1, 60),
                Setting.of("Pause duration (minutes)", pomodoroPauseProperty, 1, 60),
                Setting.of("Long pause duration (minutes)", pomodoroLongPauseProperty, 1, 60),
                Setting.of("Sessions before a long pause", numberOfSessionsBeforeLongPauseProperty)
        );

        Group screenResolutionGroup = Group.of("Screen resolution", Setting.of("Resolution", resolutionItems, resolutionSelection));
        Group screenResolutionSubGroup = Group.of("Screen resolution", Setting.of("Resolution", resolutionItems, resolutionSelection));

        Group themesGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));
        Group themesSubGroup = Group.of("Themes", Setting.of("Night Mode", nightMode));

        Group textGroup = Group.of("Text", Setting.of("Font Color", colorProperty));
        Group textSubGroup = Group.of("Text", Setting.of("Font Size", fontSize, 6, 36));

        return PreferencesFx.of(SettingsController.class,
                        Category.of("General", timerGroup)
                                .expand(),
                        Category.of("Graphics", screenResolutionGroup, themesGroup, textGroup)
//                                .expand()
                                .subCategories(
                                        Category.of("Screen", screenResolutionSubGroup),
                                        Category.of("Look and feel", themesSubGroup, textSubGroup)
                                )
//                Category.of("Languages",
//                        Group.of("Choose language",
//                                Setting.of("Language", localesListProperty, selectedLocalesProperty)
//                        )
//                )
                )
                .persistWindowState(false)
                .instantPersistent(true)
                .saveSettings(true)
                .debugHistoryMode(false)
                .buttonsVisibility(true);
    }
}

