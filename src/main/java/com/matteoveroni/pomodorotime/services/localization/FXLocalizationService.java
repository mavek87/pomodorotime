package com.matteoveroni.pomodorotime.services.localization;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
public class FXLocalizationService {

    public static final String TRANSLATIONS_NOT_LOADED_FOR_SELECTED_LOCALE = "TRANSLATIONS_NOT_LOADED_FOR_SELECTED_LOCALE";
    public static final String NO_TRANSLATION_FOUND = "NO_TRANSLATION_FOUND";

    private final Map<Locale, Translations> localizationData = new HashMap<>();
    private final ObjectProperty<Locale> selectedLocaleProperty = new SimpleObjectProperty<>();

    private class Translations {
        private final ResourceBundle resourceBundle;

        public Translations(ResourceBundle resourceBundle) {
            this.resourceBundle = resourceBundle;
        }

        public String translate(String key) {
            return resourceBundle.containsKey(key)
                    ? resourceBundle.getString(key)
                    : NO_TRANSLATION_FOUND;
        }

        public ResourceBundle getResourceBundle() {
            return resourceBundle;
        }
    }

    public FXLocalizationService(Locale locale) {
        log.debug("Locale set to => " + locale);
        selectedLocaleProperty.set(locale);
    }

    public void useLocale(Locale locale) {
        if (localizationData.containsKey(locale)) {
            selectedLocaleProperty.set(locale);
            log.debug("Locale set to => " + locale);
        }
    }

    public Optional<ResourceBundle> getCurrentlyUsedResourceBundle() {
        Locale selectedLocale = selectedLocaleProperty.get();
        if (selectedLocale != null && localizationData.containsKey(selectedLocale)) {
            return Optional.of(localizationData.get(selectedLocale).getResourceBundle());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Example of usage:
     * localization.addResourceBundle("locale", Locale.US);
     * localization.addResourceBundle("locale", Locale.ITALY);
     *
     * @param baseName Resource bundle basename
     * @param locale   Resource bundle locale
     */
    public void addResourceBundle(String baseName, Locale locale) {
        try {
            Locale.setDefault(locale);
            ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale);
            localizationData.put(locale, new Translations(resourceBundle));
        } finally {
            Locale.setDefault(selectedLocaleProperty.get());
        }
    }

    /**
     * Example to change selected locale:
     * localization.selectedLocaleProperty().bind(combobox.valueProperty());
     *
     * @return The selected locale property
     */
    public ObjectProperty<Locale> selectedLocaleProperty() {
        return selectedLocaleProperty;
    }

    /**
     * Example of usage:
     * text.textProperty().bind(localization.getLocalizedString("hello"));
     *
     * @param key Localization key
     * @return
     */
    public StringBinding getLocalizedString(String key) {
        return Bindings.createStringBinding(() -> translateLocalizedString(key, selectedLocaleProperty.get()), selectedLocaleProperty);
    }

    public String translateLocalizedString(String key) {
        return translateLocalizedString(key, selectedLocaleProperty.get());
    }

    private String translateLocalizedString(String key, Locale locale) {
        if (localizationData.containsKey(locale)) {
            return localizationData.get(locale).translate(key);
        } else {
            log.error("Translations not found for selected locale ({})", locale);
            return key;
        }
    }
}
