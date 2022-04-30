package com.matteoveroni.pomodorotime;

import com.matteoveroni.pomodorotime.utils.JavaVMSpecsPrinter;

public class Launcher {

    public static void main(String[] args) {
        JavaVMSpecsPrinter.printSpecs();
        App.main(args);
    }
}
