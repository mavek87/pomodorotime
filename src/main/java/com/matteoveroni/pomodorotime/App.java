package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.producers.ConfigProducer;
import com.matteoveroni.pomodorotime.utils.JavaVMSpecsPrinter;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Objects;
import java.util.Optional;

public class App extends Application {

    public static final SeContainer context = SeContainerInitializer.newInstance()
            .disableDiscovery()
            .addPackages(true, App.class)
            .initialize();

    private final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private Stage stage;
    private Config config;

    public static final void main(String... args) {
        System.setProperty("javafx.preloader", AppPreloader.class.getCanonicalName());
        Application.launch(App.class, args);
    }

    @Override
    public void init() throws Exception {
        JavaVMSpecsPrinter.printSpecs();

        ConfigProducer configProducer = context.select(ConfigProducer.class).get();
        config = configProducer.getConfig();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource("pomodoro.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
        stage.setTitle(config.getAppName());
        stage.getIcons().add(new Image(Objects.requireNonNull(classLoader.getResourceAsStream("icons/tomato.png"))));
        stage.setResizable(false);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });
        stage.setWidth(config.getWindowWidth());
        stage.setHeight(config.getWindowHeight());
        stage.show();

        stage.setOnCloseRequest(confirmCloseEventHandler);
    }

    private final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to quit?"
        );
        closeConfirmation.setTitle(config.getAppName());
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
