package com.matteoveroni.pomodorotime.factories;

import com.matteoveroni.pomodorotime.gui.views.View;
import com.matteoveroni.pomodorotime.services.ResourcesService;
import com.matteoveroni.routerfx.core.RouterFX;
import java.io.IOException;

public final class RouterFXRoutesFactory {

    public static void createRoutes(ResourcesService resourcesService) {
//        Arrays.stream(View.values()).forEach(view -> setupRoute(view, resourcesService));
    }

    private static void setupRoute(View route, ResourcesService resourcesService) {
        try {
            RouterFX.when(route.getId(), resourcesService.getFXMLViewURL(route.getFileName()));
        } catch (IOException ex) {
            throw new RuntimeException("Error during creation of routes", ex);
        }
    }
}

