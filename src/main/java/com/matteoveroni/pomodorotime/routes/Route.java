package com.matteoveroni.pomodorotime.routes;

public enum Route {
    POMODORO_VIEW("pomodoro", "route-title_main_menu_key"),
    SETTINGS_VIEW("settings", "route-title_settings_key");

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