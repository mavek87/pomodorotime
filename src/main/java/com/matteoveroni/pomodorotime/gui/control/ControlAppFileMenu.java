package com.matteoveroni.pomodorotime.gui.control;

import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class ControlAppFileMenu extends BorderPane implements LoadableControl, Initializable {

    private static final String KEY_MENU_HELP = "control_app_file_menu_key_menu_help";
    private static final String KEY_MENU_ITEM_POMODORO = "control_app_file_menu_key_menu_item_pomodoro";
    private static final String KEY_MENU_ITEM_SETTINGS = "control_app_file_menu_key_menu_item_settings";
    private static final String KEY_MENU_ITEM_ABOUT = "control_app_file_menu_key_menu_item_about";
    private static final String KEY_MENU_ITEM_EXIT = "control_app_file_menu_key_menu_item_exit";
    private static final String KEY_MENU_ITEM_AUTHOR = "control_app_file_menu_key_menu_item_author";
    private static final String KEY_MENU_ITEM_WEBSITE = "control_app_file_menu_key_menu_item_website";

    @FXML Menu menuHelp;
    @FXML MenuItem menuItemPomodoro;
    @FXML MenuItem menuItemSettings;
    @FXML MenuItem menuItemAbout;
    @FXML MenuItem menuItemExit;

    private final Stage stage;
    private final AppViewController appViewController;
    private final ControlPomodoro controlPomodoro;
    private final ControlSettings controlSettings;
    private final FXLocalizationService localizationService;

    public ControlAppFileMenu(Stage stage, AppViewController appViewController, ResourcesService resourcesService, ControlPomodoro controlPomodoro, ControlSettings controlSettings, FXLocalizationService localizationService) {
        this.stage = stage;
        this.appViewController = appViewController;
        this.controlPomodoro = controlPomodoro;
        this.controlSettings = controlSettings;
        this.localizationService = localizationService;
        loadControl(resourcesService, Control.APP_FILE_MENU);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
        menuHelp.textProperty().bind(localizationService.getLocalizedString(KEY_MENU_HELP));
        menuItemPomodoro.textProperty().bind(localizationService.getLocalizedString(KEY_MENU_ITEM_POMODORO));
        menuItemSettings.textProperty().bind(localizationService.getLocalizedString(KEY_MENU_ITEM_SETTINGS));
        menuItemAbout.textProperty().bind(localizationService.getLocalizedString(KEY_MENU_ITEM_ABOUT));
        menuItemExit.textProperty().bind(localizationService.getLocalizedString(KEY_MENU_ITEM_EXIT));
    }

    @FXML
    void onActionPomodoro(ActionEvent event) {
        appViewController.showNodeInView(controlPomodoro);
    }

    @FXML
    void onActionSettings(ActionEvent event) {
        appViewController.showNodeInView(controlSettings);
    }

    @FXML
    void onActionAbout(ActionEvent event) {
        showAlertAbout();
    }

    @FXML
    void onActionExit(ActionEvent event) {
        Platform.exit();
    }

    public static void openWebPage(String url) {
        SwingUtilities.invokeLater(() -> {
            try {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                }
            } catch (Exception e) {
                log.error("Error", e);
            }
        });
    }

    private void showAlertAbout() {
        appViewController.setOverlayPane(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setResizable(false);
        alert.setTitle(localizationService.translateLocalizedString(KEY_MENU_ITEM_ABOUT));
        alert.setHeaderText("Pomodoro-Time");

        BorderPane pane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.getChildren().add(new Label(localizationService.translateLocalizedString(KEY_MENU_ITEM_AUTHOR) + ": Matteo Veroni"));

        HBox hBox = new HBox();
        hBox.getChildren().add(new Label(localizationService.translateLocalizedString(KEY_MENU_ITEM_WEBSITE) + ":"));
        Hyperlink websiteHyperlink = new Hyperlink("https://github.com/mavek87/pomodorotime");
        websiteHyperlink.visitedProperty().addListener((observable, oldValue, newValue) -> {
            openWebPage(websiteHyperlink.getText());
        });
        hBox.getChildren().add(websiteHyperlink);
        vBox.getChildren().add(hBox);

        pane.setCenter(vBox);

        DialogPane alertDialogPane = alert.getDialogPane();
        alertDialogPane.setContent(pane);
        alertDialogPane.getScene().getWindow().setOnCloseRequest(Event::consume);

        alert.initOwner(stage);
        alert.showAndWait();

        appViewController.setOverlayPane(false);
    }
}