package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.utils.JavaVMSpecsPrinter;

/**
 * This is the main class.
 * Workaround to use JavaFX plugin with java version > 8
 */
public final class Launcher {

    public static final void main(String[] args) {
        JavaVMSpecsPrinter.printSpecs();
        App.main(args);
    }
}
