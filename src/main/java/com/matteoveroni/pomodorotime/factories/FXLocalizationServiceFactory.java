package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.services.localization.SupportedLocale;
import java.util.Arrays;
import java.util.Locale;

public class FXLocalizationServiceFactory {

    private static final String LOCALE_FILES_BASE_NAME = "locale";

    private final FXLocalizationService localizationService = new FXLocalizationService(FXLocalizationService.DEFAULT_LOCALE);

    public FXLocalizationServiceFactory() {
        addSupportedLocalesToLocalizationService();

        Locale systemLocale = Locale.getDefault();
        if (systemLocale != FXLocalizationService.DEFAULT_LOCALE) {
            useLocaleIfSupported(systemLocale);
        }
    }

    public FXLocalizationService create() {
        return localizationService;
    }

    private void addSupportedLocalesToLocalizationService() {
        localizationService.addResourceBundle(LOCALE_FILES_BASE_NAME, Locale.US);
        localizationService.addResourceBundle(LOCALE_FILES_BASE_NAME, Locale.ITALY);
    }

    private void useLocaleIfSupported(Locale locale) {
        if (isLocaleSupported(locale)) {
            localizationService.useLocale(locale);
        }
    }

    private boolean isLocaleSupported(Locale systemLocale) {
        return Arrays.stream(SupportedLocale.values())
                .anyMatch(supportedLocale -> supportedLocale.getLocale().equals(systemLocale));
    }

}
