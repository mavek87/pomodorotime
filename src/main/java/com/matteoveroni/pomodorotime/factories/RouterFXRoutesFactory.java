package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.routes.Route;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.routerfx.core.RouterFX;
import java.io.IOException;

public class RouterFXRoutesFactory {

    public static final void createRoutes(ResourcesService resourcesService) throws IOException {
        RouterFX.when(Route.POMODORO_VIEW.getId(), resourcesService.getPomodoroFXMLViewURL());
        RouterFX.when(Route.SETTINGS_VIEW.getId(), resourcesService.getSettingsFXMLViewURL());
    }
}

