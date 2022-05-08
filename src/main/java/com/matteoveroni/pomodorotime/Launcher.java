package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.utils.JavaVMSpecsPrinter;

public final class Launcher {

    public static final void main(String[] args) {
        JavaVMSpecsPrinter.printSpecs();
        App.main(args);
    }
}
