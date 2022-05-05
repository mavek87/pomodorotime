package com.matteoveroni.pomodorotime.gui.views;

public enum View {
    APP_VIEW("app", "app_view.fxml");

    private final String id;
    private final String fileName;

    View(String id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }
}