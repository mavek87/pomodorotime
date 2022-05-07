package com.matteoveroni.pomodorotime.utils;

import javafx.util.Duration;
import java.util.concurrent.TimeUnit;

public final class DurationFormatter {

    public static String formatElapsedDurationTime(Duration elapsedDuration) {
        return convertSecondsToHHMMSS((long) elapsedDuration.toSeconds());
    }

    public static String formatRemainingDurationTime(double totalDurationInMinutes, Duration elapsedDuration) {
        return convertSecondsToHHMMSS((long) ((totalDurationInMinutes * 60L) - elapsedDuration.toSeconds()));
    }

    public static String convertSecondsToHHMMSS(long seconds) {
        final long HH = TimeUnit.SECONDS.toHours(seconds);
        final long MM = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        final long SS = TimeUnit.SECONDS.toSeconds(seconds) % 60;
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}
