package com.matteoveroni.pomodorotime.gui.control;

public enum Control {
    APP_FILE_MENU("app-file-menu", "control_app_file_menu.fxml"),
    POMODORO("pomodoro", "control_pomodoro.fxml"),
    SETTINGS("settings", "control_settings.fxml");

    private final String id;
    private final String controlFileName;

    Control(String id, String viewFileName) {
        this.id = id;
        this.controlFileName = viewFileName;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return controlFileName;
    }
}