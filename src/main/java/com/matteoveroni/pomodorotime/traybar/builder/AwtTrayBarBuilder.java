package com.matteoveroni.pomodorotime.traybar.builder;

import java.awt.*;
import com.matteoveroni.pomodorotime.App;
import com.matteoveroni.pomodorotime.services.ImageService;
import com.matteoveroni.pomodorotime.traybar.AwtTrayBar;
import com.matteoveroni.pomodorotime.traybar.dto.AwtTrayBarData;
import com.matteoveroni.pomodorotime.traybar.TrayBar;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwtTrayBarBuilder {

    private static final Logger log = LoggerFactory.getLogger(AwtTrayBarBuilder.class);

    /**
     * Sets up a system tray icon for the application.
     */
    public TrayBar build(Stage stage) throws Exception {
//        try {
        // ensure awt toolkit is initialized.
        Toolkit.getDefaultToolkit();

        // app requires system tray support, just exit if there is no support.
        if (!SystemTray.isSupported()) {
            log.error("No system tray support, application exiting.");
            Platform.exit();
        }

        MenuItem menuItemShowAppWindow = new MenuItem("Show app window");
        // the convention for tray icons seems to be to set the default icon for opening
        // the application stage in a bold font.
        Font defaultFont = Font.decode(null);
        Font boldFont = defaultFont.deriveFont(Font.BOLD);
        menuItemShowAppWindow.setFont(boldFont);

        MenuItem menuItemCloseAppWindow = new MenuItem("Close app window");
        MenuItem menuItemExit = new MenuItem("Exit");

        // setup the popup menu for the application.
        final PopupMenu popup = new PopupMenu();
        popup.add(menuItemShowAppWindow);
        popup.addSeparator();
        popup.add(menuItemCloseAppWindow);
        popup.addSeparator();
        popup.add(menuItemExit);

        // set up a system tray icon.
        SystemTray tray = SystemTray.getSystemTray();
        // add the application tray icon to the system tray.
        Image imageGiuffreIcon = ImageIO.read(ImageService.APP_LOGO_ICON_URL);
        TrayIcon trayIcon = new TrayIcon(imageGiuffreIcon, App.APP_TITLE);
        trayIcon.setImageAutoSize(true);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);

        AwtTrayBarData awtTrayBarData = new AwtTrayBarData(menuItemShowAppWindow, menuItemCloseAppWindow, menuItemExit);
        TrayBar trayBar = new AwtTrayBar(tray, trayIcon, awtTrayBarData, stage);

        // if the user double-clicks on the tray icon, show the main app stage.
        trayIcon.addActionListener(event -> trayBar.showAppWindow());
        // if the user selects the default menu item (which includes the app name),
        // show the main app stage.
        menuItemShowAppWindow.addActionListener(event -> trayBar.showAppWindow());
        menuItemCloseAppWindow.addActionListener(event -> trayBar.closeWindow());
        // to really exit the application, the user must go to the system tray icon
        // and select the exit option, this will shutdown JavaFX and remove the
        // tray icon (removing the tray icon will also shut down AWT).
        menuItemExit.addActionListener(event -> trayBar.exit());

        return trayBar;
    }
}
