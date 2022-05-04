package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.factories.ControllersFactory;
import com.matteoveroni.pomodorotime.routes.Route;
import com.matteoveroni.pomodorotime.factories.RouterFXRoutesFactory;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.pomodorotime.singleton.ConfigSingleton;
import com.matteoveroni.routerfx.core.RoutedWindow;
import com.matteoveroni.routerfx.core.RouterFX;
import com.matteoveroni.routerfx.dto.WindowSize;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
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
        this.controllersFactory = new ControllersFactory(stage, resourcesService, config);

        stage.getIcons().add(new Image(Objects.requireNonNull(resourcesService.getLogoIconURL().openStream())));
        stage.setOnCloseRequest(confirmCloseEventHandler);

        RoutedWindow routedWindow = RoutedWindow.builder(stage)
                .title(config.getAppName())
                .resizableByDefault(false)
                .windowSize(new WindowSize(config.getWindowWidth(), config.getWindowHeight()))
                .centerWindowsOnShown(true)
                .build();

        RouterFX.init(routedWindow, controllersFactory);
        RouterFXRoutesFactory.createRoutes(resourcesService);
        RouterFX.goTo(Route.POMODORO_VIEW.getId());

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
