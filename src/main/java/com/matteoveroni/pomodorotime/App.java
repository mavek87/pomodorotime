package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.gui.control.ControlPomodoro;
import com.matteoveroni.pomodorotime.gui.control.ControlSettings;
import com.matteoveroni.pomodorotime.factories.ControllersFactory;
import com.matteoveroni.pomodorotime.utils.FXGraphicsUtils;
import com.matteoveroni.pomodorotime.gui.views.View;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.singleton.ConfigSingleton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Objects;
import java.util.Optional;

public class App extends Application {

    private ResourcesService resourcesService;
    private ControllersFactory controllersFactory;
    private Stage stage;
    private Config config;

    public static final void main(String... args) {
        System.setProperty("javafx.preloader", AppPreloader.class.getCanonicalName());
        Application.launch(App.class, args);
    }

    @Override
    public void init() {
        resourcesService = new ResourcesService();
        config = ConfigSingleton.INSTANCE.getConfig();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        ControlPomodoro controlPomodoro = new ControlPomodoro(stage, resourcesService);
        ControlSettings controlSettings = new ControlSettings(config, resourcesService);
        this.controllersFactory = new ControllersFactory(stage, resourcesService, config, controlPomodoro, controlSettings);

        FXMLLoader fxmlLoader = new FXMLLoader(resourcesService.getFXMLViewURL(View.APP_VIEW.getFileName()));
        fxmlLoader.setControllerFactory(controllersFactory);
        Pane pane = fxmlLoader.load();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle(config.getAppName());
        stage.getIcons().add(new Image(Objects.requireNonNull(resourcesService.getLogoIconURL().openStream())));
        stage.setResizable(false);
        stage.setWidth(config.getWindowWidth());
        stage.setHeight(config.getWindowHeight());
        stage.setOnCloseRequest(confirmCloseEventHandler);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> FXGraphicsUtils.centerStage(stage));
        stage.show();

//        FXTrayIcon icon = new FXTrayIcon(this.stage, resourcesService.getLogoIconURL());
//        icon.show();
    }

    private final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert quitConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?");
        quitConfirmationAlert.setTitle(config.getAppName());
        quitConfirmationAlert.setHeaderText("Exit confirmation");
        quitConfirmationAlert.initModality(Modality.APPLICATION_MODAL);
        quitConfirmationAlert.initOwner(stage);
        Button exitButton = (Button) quitConfirmationAlert.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        Optional<ButtonType> closeButton = quitConfirmationAlert.showAndWait();
        if (!ButtonType.OK.equals(closeButton.get())) {
            event.consume();
        }
    };
}
