package com.matteoveroni.pomodorotime;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Optional;

public class App extends Application {

    private final Stage stage = new Stage();

    public static final void main(String... args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage unusedStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("pomodoro.fxml"));
        Pane pane = fxmlLoader.load();
//        BorderPane p = new BorderPane();
//        p.setCenter(new Button("ciao"));
        stage.setScene(new Scene(pane));
        stage.setTitle("Pomodoro-Time");
        stage.setWidth(1024);
        stage.setHeight(768);
//        stage.setOnHiding(event -> trayBar.chiudiFinestra());
        stage.show();

//        if (isAppUsingTrayBar) {
//            Platform.setImplicitExit(false);
//        } else {
//            stage.setOnCloseRequest(confirmCloseEventHandler);
//        }
    }

    private final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Sei sicuro di voler uscire?"
        );
        closeConfirmation.setHeaderText("Conferma uscita");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(stage);
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Esci");
        Optional<ButtonType> closeButton = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeButton.get())) {
            event.consume();
        }
    };
}
