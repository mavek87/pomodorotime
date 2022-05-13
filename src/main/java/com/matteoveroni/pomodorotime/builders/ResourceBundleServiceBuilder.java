package com.matteoveroni.pomodorotime.builders;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import java.util.ResourceBundle;

public record ResourceBundleServiceBuilder(FXLocalizationService localizationService) {

    public ResourceBundleService build() {
        final ResourceBundle resourceBundle = localizationService.getCurrentlyUsedResourceBundle().orElseThrow(() -> new IllegalStateException("Error, current resource bundle is not set"));
        final ResourceBundleService resourceBundleService = new ResourceBundleService(resourceBundle);
        localizationService.selectedLocaleProperty().addListener((observable, oldValue, newValue) ->
                resourceBundleService.changeLocale(localizationService.getCurrentlyUsedResourceBundle().get())
        );
        return resourceBundleService;
    }
}
