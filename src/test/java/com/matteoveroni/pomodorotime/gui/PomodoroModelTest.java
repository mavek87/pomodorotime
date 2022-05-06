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
        when(mockConfig.getPomodoroDuration()).thenReturn(1);

        assertEquals(1, mockConfigManager.readConfig().getPomodoroDuration(), "Error, something is wrong with the mocks...");
    }

    @Test
    public void pomodoro_session_is_zero_if_pomodoro_is_never_being_started() {
        assertEquals(0, pomodoroModel.getPomodoroSession(), "Error, pomodoro session is not zero when pomodoro is never being stopped");
    }

    @Test
    public void pomodoro_session_is_one_if_pomodoro_is_started_once() {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);

        pomodoroModel.start();

        assertEquals(1, pomodoroModel.getPomodoroSession(), "Error, pomodoro session is not one when pomodoro is being started once");
    }

    @Test
    public void calling_start_method_twice_consecutively_throws_illegal_state_exception() {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);

        assertThrows(IllegalStateException.class, () -> {
            pomodoroModel.start();
            pomodoroModel.start();
        });
    }

    @Test
    public void calling_stop_method_twice_consecutively_throws_illegal_state_exception() {
        assertThrows(IllegalStateException.class, () -> {
            pomodoroModel.stop();
            pomodoroModel.stop();
        });
    }

    @Test
    public void pomodoro_session_is_two_if_pomodoro_is_started_twice() {
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);

        pomodoroModel.start();
        pomodoroModel.stop();
        pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(2, pomodoroModel.getPomodoroSession(), "Error, pomodoro session is not two when pomodoro is being started twice");
    }

    @Test
    public void number_of_sessions_before_long_pause_equal_to_zero_so_first_time_long_pause() {
        final int numberOfSessionsBeforeLongPause = 0;
        final int longPauseDuration = 10;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroLongPauseDuration()).thenReturn(longPauseDuration);

        final int pomodoroPauseDuration = pomodoroModel.start();

        assertEquals(longPauseDuration, pomodoroPauseDuration,"Error");
    }

    @Test
    public void number_of_sessions_before_long_pause_equal_to_one_so_first_time_short_pause() {
        final int numberOfSessionsBeforeLongPause = 1;
        final int shortPauseDuration = 5;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroPauseDuration()).thenReturn(shortPauseDuration);

        final int pomodoroPauseDuration = pomodoroModel.start();

        assertEquals(shortPauseDuration, pomodoroPauseDuration,"Error");
    }

    @Test
    public void number_of_sessions_before_long_pause_equal_to_two_so_first_two_times_short_pause_and_then_long_pause_and_after_short_pause() {
        final int numberOfSessionsBeforeLongPause = 2;
        final int longPauseDuration = 10;
        final int shortPauseDuration = 5;
        when(mockConfigManager.readConfig()).thenReturn(mockConfig);
        when(mockConfig.getNumberOfSessionBeforeLongPause()).thenReturn(numberOfSessionsBeforeLongPause);
        when(mockConfig.getPomodoroLongPauseDuration()).thenReturn(longPauseDuration);
        when(mockConfig.getPomodoroPauseDuration()).thenReturn(shortPauseDuration);

        final int firstPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, firstPomodoroPauseDuration,"Error");

        final int secondPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, secondPomodoroPauseDuration,"Error");

        final int thirdPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(longPauseDuration, thirdPomodoroPauseDuration,"Error");

        final int fourthPomodoroPauseDuration = pomodoroModel.start();
        pomodoroModel.stop();

        assertEquals(shortPauseDuration, fourthPomodoroPauseDuration,"Error");
    }
}