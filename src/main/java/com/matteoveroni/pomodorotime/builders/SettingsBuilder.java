package com.matteoveroni.pomodorotime.builders;

import com.matteoveroni.pomodorotime.Settings;
import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.gui.screen.ScreenResolution;
import com.matteoveroni.pomodorotime.services.localization.FXLocalizationService;
import com.matteoveroni.pomodorotime.services.localization.SupportedLocale;
import static com.matteoveroni.pomodorotime.Settings.DEFAULT_SCREEN_SIZE_RESOLUTION;

public record SettingsBuilder(Config config, FXLocalizationService localizationService) {

    public Settings build() {
        final Settings settings = new Settings();
        final String language = config.getLanguage();
        final SupportedLocale configSupportedLocale = SupportedLocale.fromString(language).orElse(SupportedLocale.US);
        localizationService.useLocale(configSupportedLocale.getLocale());
        settings.setSelectedLocale(configSupportedLocale);

        final double windowWidth = config.getWindowWidth();
        final double windowHeight = config.getWindowHeight();
        settings.setResolutionSelection(ScreenResolution.fromSize(windowWidth, windowHeight).orElse(DEFAULT_SCREEN_SIZE_RESOLUTION));

        settings.setPomodoroDuration(config.getPomodoroDuration());
        settings.setPomodoroPause(config.getPomodoroPauseDuration());
        settings.setPomodoroLongPause(config.getPomodoroLongPauseDuration());
        settings.setNumberOfSessionsBeforeLongPause(config.getNumberOfSessionBeforeLongPause());
        settings.setPlayAlarmSoundOnCompletion(config.isPlayAlarmSoundOnCompletion());
        settings.setAllowInterruptPomodoro(config.isAllowInterruptPomodoro());
        settings.setAllowAbortPomodoro(config.isAllowAbortPomodoro());
        settings.setIsPomodoroLoop(config.isPomodoroLoop());
        settings.setIsFullScreenPauseAlertOn(config.isPomodoroPauseAlertFullscreen());
        settings.setAllowInterruptPause(config.isAllowInterruptPause());
        settings.setAllowAbortPause(config.isAllowAbortPause());
        return settings;
    }
}
