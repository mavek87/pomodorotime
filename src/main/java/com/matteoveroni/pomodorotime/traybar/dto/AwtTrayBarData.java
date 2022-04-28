package com.matteoveroni.pomodorotime.traybar.dto;

import java.awt.*;

public class AwtTrayBarData {
    private final MenuItem menuItemShowAppWindow;
    private final MenuItem menuItemCloseWindow;
    private final MenuItem menuItemExit;

    public AwtTrayBarData(MenuItem menuItemShowAppWindow, MenuItem menuItemCloseWindow, MenuItem menuItemExit) {
        this.menuItemShowAppWindow = menuItemShowAppWindow;
        this.menuItemCloseWindow = menuItemCloseWindow;
        this.menuItemExit = menuItemExit;
    }

    public MenuItem getMenuItemShowAppWindow() {
        return menuItemShowAppWindow;
    }

    public MenuItem getMenuItemCloseWindow() {
        return menuItemCloseWindow;
    }

    public MenuItem getMenuItemExit() {
        return menuItemExit;
    }
}
