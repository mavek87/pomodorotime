package com.matteoveroni.pomodorotime.traybar.dto;


import dorkbox.systemTray.MenuItem;

public class DorkerTrayBarData {

    private final MenuItem menuItemShowAppWindow;
    private final MenuItem menuItemChiudiFinestra;
    private final dorkbox.systemTray.MenuItem menuItemExit;

    public DorkerTrayBarData(MenuItem menuItemShowAppWindow, MenuItem menuItemChiudiFinestra, MenuItem menuItemExit) {
        this.menuItemShowAppWindow = menuItemShowAppWindow;
        this.menuItemChiudiFinestra = menuItemChiudiFinestra;
        this.menuItemExit = menuItemExit;
    }

    public MenuItem getMenuItemShowAppWindow() {
        return menuItemShowAppWindow;
    }

    public MenuItem getMenuItemCloseWindow() {
        return menuItemChiudiFinestra;
    }

    public MenuItem getMenuItemExit() {
        return menuItemExit;
    }
}

