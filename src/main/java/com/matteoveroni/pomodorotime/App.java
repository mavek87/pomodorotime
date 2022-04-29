package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.services.ImageService;
import com.matteoveroni.pomodorotime.traybar.TrayBar;
import com.matteoveroni.pomodorotime.traybar.builder.CrossPlatformTrayBarBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.util.Optional;

public class App extends Application {

    public static final String APP_TITLE = "Pomodoro-Time";
    private static final double WINDOW_HEIGHT = 1024;
    private static final double WINDOW_WIDTH = 768;
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static boolean isAppUsingTrayBar = false;

    private final Stage stage = new Stage();
//    private final CrossPlatformTrayBarBuilder trayBarBuilder = new CrossPlatformTrayBarBuilder(stage);

//    private TrayBar trayBar;

    public static final void main(String... args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
//        trayBarBuilder.build().ifPresent(trayBarCostruita -> trayBar = trayBarCostruita);
//        isAppUsingTrayBar = trayBar != null;
        log.info("javaVersion: {}", System.getProperty("java.version"));
        log.info("javaVersionDate: " + System.getProperty("java.version.date"));
        log.info("javaVmName: " + System.getProperty("java.vm.name"));
        log.info("javaVendor: {}", System.getProperty("java.vendor"));
        log.info("vmVersion: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
    }

    @Override
    public void start(Stage unusedStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("pomodoro.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
        stage.setTitle(APP_TITLE);
        stage.getIcons().add(ImageService.APP_LOGO_ICON_IMAGE);
        stage.setResizable(false);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });
//        stage.setOnHiding(event -> trayBar.closeWindow());
        stage.setWidth(WINDOW_HEIGHT);
        stage.setHeight(WINDOW_WIDTH);
        stage.show();

        if (isAppUsingTrayBar) {
            Platform.setImplicitExit(false);
        } else {
            stage.setOnCloseRequest(confirmCloseEventHandler);
        }
    }

    private final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to quit?"
        );
        closeConfirmation.setHeaderText("Exit confirmation");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(stage);
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        Optional<ButtonType> closeButton = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeButton.get())) {
            event.consume();
        }
    };
}
