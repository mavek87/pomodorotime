package com.matteoveroni.pomodorotime.traybar.builder;

import com.matteoveroni.pomodorotime.App;
import com.matteoveroni.pomodorotime.services.ImageService;
import com.matteoveroni.pomodorotime.traybar.DorkerTrayBar;
import com.matteoveroni.pomodorotime.traybar.dto.DorkerTrayBarData;
import com.matteoveroni.pomodorotime.traybar.TrayBar;
import com.matteoveroni.pomodorotime.traybar.exceptions.TrayBarNotSupportedException;
import dorkbox.os.OS;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class DorkerTrayBarBuilder {

    private static final Logger log = LoggerFactory.getLogger(DorkerTrayBarBuilder.class);

    public TrayBar build(Stage stage) throws Exception {
        log.info("{} builder start", getClass().getSimpleName());

        SystemTray.DEBUG = true;

        // ATTENZIONE: Ubuntu usa gtk3 che non è compatibile con java 8 ma solo con java 11
        // Usare GTK2, oppure usare jdk 11 durante per poter testare la tray bar anche su linux
        if (OS.isLinux()) {
            SystemTray.PREFER_GTK3 = false;
            SystemTray.FORCE_GTK2 = true;
        } else {
            setSwingNimbusLookAndFeel();
        }

        SystemTray systemTray = SystemTray.get(App.APP_TITLE);
        if (systemTray == null) {
            throw new TrayBarNotSupportedException("Tray bar not supported");
        }
        systemTray.installShutdownHook();
        systemTray.setImage(ImageService.APP_LOGO_ICON_URL);

        Menu mainMenu = systemTray.getMenu();

        MenuItem menuItemShowAppWindow = new MenuItem("Show app window");
        MenuItem menuItemCloseWindow = new MenuItem("Close app window");
        MenuItem menuItemExit = new MenuItem("Exit");

        mainMenu.add(menuItemShowAppWindow);
        mainMenu.add(menuItemCloseWindow);
        mainMenu.add(menuItemExit);

        DorkerTrayBarData data = new DorkerTrayBarData(menuItemShowAppWindow, menuItemCloseWindow, menuItemExit);
        TrayBar trayBar = new DorkerTrayBar(systemTray, data, stage);

        menuItemShowAppWindow.setCallback(action -> trayBar.showAppWindow());
        menuItemCloseWindow.setCallback(action -> trayBar.closeWindow());
        menuItemExit.setCallback(action -> trayBar.exit());

        log.info("{} builder finish", getClass().getSimpleName());
        return trayBar;
    }

    private static void setSwingNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available do nothing and use the default swing look and feel (metal).
        }
    }
}
