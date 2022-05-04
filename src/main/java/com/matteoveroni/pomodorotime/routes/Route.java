package com.matteoveroni.pomodorotime.routes;

public enum Route {
    POMODORO_VIEW("pomodoro", "route-view_pomodoro_title_key"),
    SETTINGS_VIEW("settings", "route-view_settings_title_key");

    private final String id;
    private final String localizedTitleKey;

    Route(String id, String localizedTitleKey) {
        this.id = id;
        this.localizedTitleKey = localizedTitleKey;
    }

    public String getId() {
        return id;
    }

    public String getLocalizedTitleKey() {
        return localizedTitleKey;
    }
}