package com.matteoveroni.pomodorotime.traybar;

import com.matteoveroni.pomodorotime.traybar.dto.AwtTrayBarData;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.*;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class AwtTrayBar implements TrayBar {

    private final SystemTray tray;
    private final TrayIcon trayIcon;
    private final AwtTrayBarData awtTrayBarData;
    private final Stage stage;

    public AwtTrayBar(SystemTray tray, TrayIcon trayIcon, AwtTrayBarData awtTrayBarData, Stage stage) {
        this.tray = tray;
        this.trayIcon = trayIcon;
        this.awtTrayBarData = awtTrayBarData;
        this.stage = stage;
    }

    @Override
    public void showAppWindow() {
        Platform.runLater(() -> {
            this.awtTrayBarData.getMenuItemCloseWindow().setEnabled(true);
            this.stage.show();
        });
    }

    @Override
    public void closeWindow() {
        Platform.runLater(() -> {
            this.awtTrayBarData.getMenuItemCloseWindow().setEnabled(false);
            this.stage.hide();
        });
    }

    @Override
    public void exit() {
        Platform.runLater(() -> {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            tray.remove(trayIcon);
            Platform.exit();
        });
    }
}