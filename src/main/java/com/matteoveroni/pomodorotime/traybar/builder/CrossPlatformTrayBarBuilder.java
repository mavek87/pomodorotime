package com.matteoveroni.pomodorotime.traybar.builder;

import com.matteoveroni.pomodorotime.traybar.TrayBar;
import dorkbox.os.OS;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

public class CrossPlatformTrayBarBuilder {

    private static final Logger log = LoggerFactory.getLogger(CrossPlatformTrayBarBuilder.class);
    private final AwtTrayBarBuilder awtTrayBarBuilder = new AwtTrayBarBuilder();
    private final DorkerTrayBarBuilder dorkerTrayBarBuilder = new DorkerTrayBarBuilder();
    private final Stage stage;

    public CrossPlatformTrayBarBuilder(Stage stage) {
        this.stage = stage;
    }

    public Optional<TrayBar> build() {
        Optional<TrayBar> trayBar = Optional.empty();

        boolean isAwtCompatibile = false;
        if (OS.isWindows() || OS.isMacOsX()) {
            try {
                trayBar = Optional.of(awtTrayBarBuilder.build(stage));
                isAwtCompatibile = true;
            } catch (Exception ex) {
                log.error("Error trying to build the awt traybar", ex);
            }
        }

        if (!isAwtCompatibile) {
            try {
                trayBar = Optional.of(dorkerTrayBarBuilder.build(stage));
            } catch (Exception ex) {
                log.error("Error, traybar not supported!", ex);
            }
        }

        return trayBar;
    }
}
