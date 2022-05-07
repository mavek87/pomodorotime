package com.matteoveroni.pomodorotime.gui;

import com.matteoveroni.pomodorotime.configs.Config;
import com.matteoveroni.pomodorotime.configs.ConfigManager;
import com.matteoveroni.pomodorotime.gui.model.PomodoroModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PomodoroModelTest {

    @Mock private ConfigManager mockConfigManager;
    @Mock private Config mockConfig;

    private PomodoroModel pomodoroModel;

    @BeforeEach
    void initTests() {
        pomodoroModel = new PomodoroModel(mockConfigManager);
    }

    @Test
    public void check_mocks_working () {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getPomodoroDuration()).thenReturn(1.0);

        assertEquals(1, mockConfigManager.readConfig().getPomodoroDuration(), "Error, something is wrong with the mocks...");
    }

    @Test
    public void pomodoro_session_is_zero_if_pomodoro_is_never_being_started() {
        assertEquals(0, pomodoroModel.getPomodoroSession(), "Error, pomodoro session is not zero when pomodoro is never being stopped");
    }

    @Test
    public void pomodoro_session_is_one_if_pomodoro_is_started_once() {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(1);

        pomodoroModel.start();

        assertEquals(1, pomodoroModel.getPomodoroSession(), "Error, pomodoro session is not one when pomodoro is being started once");
    }

    @Test
    public void throws_illegal_state_exception_trying_to_stop_not_running_pomodoro() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            pomodoroModel.stop();
        });
        assertEquals(PomodoroModel.ERROR_STOP_NO_RUNNING_POMODORO, ex.getMessage(), "Error");
    }

    @Test
    public void throws_illegal_state_exception_calling_trying_to_start_already_running_pomodoro() {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(1);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            pomodoroModel.start();
            pomodoroModel.start();
        });
        assertEquals(PomodoroModel.ERROR_START_ALREADY_RUNNING_POMODORO, ex.getMessage(), "Error");
    }

    @Test
    public void pomodoro_session_is_two_if_pomodoro_is_started_twice() {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.isPomodoroLoop()).thenReturn(true);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(3);

        pomodoroModel.start();
        pomodoroModel.stop();
        pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(2, pomodoroModel.getPomodoroSession(), "Error, pomodoro session is not two when pomodoro is being started twice");
    }

    @Test
    public void number_of_sessions_before_long_pause_equal_to_one_so_first_time_long_pause() {
        final int numberOfSessionsBeforeLongPause = 1;
        final double longPauseDuration = 10;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroLongPauseDuration()).thenReturn(longPauseDuration);

        final double pomodoroPauseDuration = pomodoroModel.start();

        assertEquals(longPauseDuration, pomodoroPauseDuration,"Error");
    }

    @Test
    public void number_of_sessions_before_long_pause_equal_to_two_so_first_time_short_pause() {
        final int numberOfSessionsBeforeLongPause = 2;
        final double shortPauseDuration = 5;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroPauseDuration()).thenReturn(shortPauseDuration);

        final double pomodoroPauseDuration = pomodoroModel.start();

        assertEquals(shortPauseDuration, pomodoroPauseDuration,"Error");
    }

    @Test
    public void throws_exception_trying_to_start_already_completed_pomodoro() {
        final int numberOfSessionsBeforeLongPause = 2;
        final double longPauseDuration = 10;
        final double shortPauseDuration = 5;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.isPomodoroLoop()).thenReturn(false);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroLongPauseDuration()).thenReturn(longPauseDuration);
        when(mockConfig.getPomodoroPauseDuration()).thenReturn(shortPauseDuration);

        final double firstPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, firstPomodoroPauseDuration,"Error");

        final double secondPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(longPauseDuration, secondPomodoroPauseDuration,"Error");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> pomodoroModel.start());
        assertEquals(PomodoroModel.ERROR_START_ALREADY_COMPLETED_POMODORO, ex.getMessage(), "Error");
    }

    @Test
    public void number_of_sessions_before_long_pause_equal_to_three_so_first_two_times_short_pause_and_then_long_pause_and_after_short_pause() {
        final int numberOfSessionsBeforeLongPause = 3;
        final double longPauseDuration = 10;
        final double shortPauseDuration = 5;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.isPomodoroLoop()).thenReturn(true);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroLongPauseDuration()).thenReturn(longPauseDuration);
        when(mockConfig.getPomodoroPauseDuration()).thenReturn(shortPauseDuration);

        final double firstPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, firstPomodoroPauseDuration,"Error");

        final double secondPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, secondPomodoroPauseDuration,"Error");

        final double thirdPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(longPauseDuration, thirdPomodoroPauseDuration,"Error");

        final double fourthPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, fourthPomodoroPauseDuration,"Error");
    }
}