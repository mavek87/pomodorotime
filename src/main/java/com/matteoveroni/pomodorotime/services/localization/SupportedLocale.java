package com.matteoveroni.pomodorotime.services.localization;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum SupportedLocale {
    US(Locale.US, "English (US)"),
    ITALY(Locale.ITALY, "Italiano");

    private final Locale locale;
    private final String description;

    SupportedLocale(Locale locale, String description) {
        this.locale = locale;
        this.description = description;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    public static Optional<SupportedLocale> fromLocale(Locale locale) {
        return Arrays.stream(SupportedLocale.values())
                .filter(supportedLocale -> locale.equals(supportedLocale.getLocale()))
                .findFirst();
    }

    public static Optional<SupportedLocale> fromString(String text) {
        return Arrays.stream(SupportedLocale.values())
                .filter(supportedLocale -> text.equalsIgnoreCase(supportedLocale.getLocale().toString()))
                .findFirst();
    }

}
