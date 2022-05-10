package com.matteoveroni.pomodorotime.gui.control;

import com.matteoveroni.pomodorotime.gui.controllers.AppViewController;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
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
import static com.matteoveroni.pomodorotime.utils.Constants.AUTHOR_NAME;
import static com.matteoveroni.pomodorotime.utils.Constants.LocalizationKeys.*;
import static com.matteoveroni.pomodorotime.utils.Constants.WEBSITE_URL;

@Slf4j
public class ControlAppFileMenu extends BorderPane implements LoadableControl, Initializable {

    @FXML Menu menuHelp;
    @FXML MenuItem menuItemPomodoro;
    @FXML MenuItem menuItemSettings;
    @FXML MenuItem menuItemAbout;
    @FXML MenuItem menuItemExit;

    private final Stage stage;
    private final AppViewController appViewController;
    private final ResourcesService resourcesService;
    private final ControlPomodoro controlPomodoro;
    private final ControlSettings controlSettings;
    private final FXLocalizationService localizationService;

    public ControlAppFileMenu(Stage stage, AppViewController appViewController, ResourcesService resourcesService, ControlPomodoro controlPomodoro, ControlSettings controlSettings, FXLocalizationService localizationService) {
        this.stage = stage;
        this.appViewController = appViewController;
        this.resourcesService = resourcesService;
        this.controlPomodoro = controlPomodoro;
        this.controlSettings = controlSettings;
        this.localizationService = localizationService;
        loadControl(resourcesService, Control.APP_FILE_MENU);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("INITIALIZE " + getClass().getSimpleName());
        menuHelp.textProperty().bind(localizationService.getLocalizedString(HELP));
        menuItemPomodoro.textProperty().bind(localizationService.getLocalizedString(POMODORO));
        menuItemSettings.textProperty().bind(localizationService.getLocalizedString(SETTINGS));
        menuItemAbout.textProperty().bind(localizationService.getLocalizedString(ABOUT));
        menuItemExit.textProperty().bind(localizationService.getLocalizedString(EXIT));
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

        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setResizable(false);
        alert.setTitle(localizationService.translateLocalizedString(ABOUT));
        alert.setHeaderText("Pomodoro-Time");

        final BorderPane pane = new BorderPane();
        final VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().add(new Label(localizationService.translateLocalizedString(AUTHOR) + ": " + AUTHOR_NAME));

        final HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(new Label(localizationService.translateLocalizedString(WEBSITE) + ": "));
        final Hyperlink websiteHyperlink = new Hyperlink(WEBSITE_URL);
        websiteHyperlink.setBorder(Border.EMPTY);
        websiteHyperlink.setPadding(new Insets(4, 0, 4, 0));
        websiteHyperlink.visitedProperty().addListener((observable, oldValue, newValue) -> openWebPage(websiteHyperlink.getText()));
        hBox.getChildren().add(websiteHyperlink);
        vBox.getChildren().add(hBox);

        vBox.getChildren().add(new Label(localizationService.translateLocalizedString(VERSION) + ": " + resourcesService.readVersion()));

        pane.setCenter(vBox);

        final DialogPane alertDialogPane = alert.getDialogPane();
        alertDialogPane.setContent(pane);
        alertDialogPane.getScene().getWindow().setOnCloseRequest(Event::consume);

        alert.initOwner(stage);
        alert.showAndWait();

        appViewController.setOverlayPane(false);
    }
}