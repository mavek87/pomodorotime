package com.matteoveroni.pomodorotime.traybar;

import com.matteoveroni.pomodorotime.traybar.dto.DorkerTrayBarData;
import dorkbox.systemTray.SystemTray;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DorkerTrayBar implements TrayBar {

    private final SystemTray tray;
    private final DorkerTrayBarData dorkerTrayBarData;
    private final Stage stage;

    public DorkerTrayBar(SystemTray tray, DorkerTrayBarData dorkerTrayBarData, Stage stage) {
        this.tray = tray;
        this.dorkerTrayBarData = dorkerTrayBarData;
        this.stage = stage;
    }

    @Override
    public void showAppWindow() {
        Platform.runLater(() -> {
            this.dorkerTrayBarData.getMenuItemCloseWindow().setEnabled(true);
            this.stage.show();
        });
    }

    @Override
    public void closeWindow() {
        Platform.runLater(() -> {
            this.dorkerTrayBarData.getMenuItemCloseWindow().setEnabled(false);
            this.stage.hide();
        });
    }

    @Override
    public void exit() {
        Platform.runLater(() -> {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            tray.shutdown();
            Platform.exit();
        });
    }
}
