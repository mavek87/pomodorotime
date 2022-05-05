package com.matteoveroni.pomodorotime.gui.screen;

import java.util.Optional;

public enum ScreenResolution {
    RESOLUTION_1920x1080(1920.0f, 1080.0f),
    RESOLUTION_1440x900(1440.0f, 900.0f),
    RESOLUTION_1280x1024(1280.0f, 1024.0f),
    RESOLUTION_1024x768(1024.0f, 768.0f);

    private final double width;
    private final double height;

    ScreenResolution(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return convertWidthAndHeightToString(width, height);
    }

    public static Optional<ScreenResolution> fromSize(double width, double height) {
        String strSize = convertWidthAndHeightToString(width, height);
        for (ScreenResolution resolution : ScreenResolution.values()) {
            if (resolution.toString().equalsIgnoreCase(strSize)) {
                return Optional.of(resolution);
            }
        }
        return Optional.empty();
    }

    public static Optional<ScreenResolution> fromString(String text) {
        for (ScreenResolution resolution : ScreenResolution.values()) {
            if (resolution.toString().equalsIgnoreCase(text)) {
                return Optional.of(resolution);
            }
        }
        return Optional.empty();
    }

    private static String convertWidthAndHeightToString(double width, double height) {
        return String.format("%.0fx%.0f", width, height);
    }
}