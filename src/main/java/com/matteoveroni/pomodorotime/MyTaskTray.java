package com.matteoveroni.pomodorotime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MyTaskTray {
    public static void main(String arg[]) throws IOException {
        final Frame frame = new Frame();
        frame.setUndecorated(true);
        // Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("tomato.png")), "Library Drop");
        final SystemTray tray = SystemTray.getSystemTray();

        // add the application tray icon to the system tray.
        trayIcon.setImageAutoSize(true);

        // Create a pop-up menu components
        final PopupMenu popup = createPopupMenu();
        trayIcon.setPopupMenu(popup);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    frame.add(popup);
                    popup.show(frame, e.getXOnScreen(), e.getYOnScreen());
                }
            }
        });
        try {
            frame.setResizable(false);
            frame.setVisible(true);
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    protected static PopupMenu createPopupMenu() {
        final PopupMenu popup = new PopupMenu();

        MenuItem aboutItem = new MenuItem("About");
        // the convention for tray icons seems to be to set the default icon for opening
        // the application stage in a bold font.
        Font defaultFont = Font.decode(null);
        Font boldFont = defaultFont.deriveFont(Font.BOLD);
        aboutItem.setFont(boldFont);
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "www.java2s.com"));

        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        // Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
        return popup;
    }
}