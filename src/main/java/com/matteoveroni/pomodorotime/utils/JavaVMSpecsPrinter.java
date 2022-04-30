package com.matteoveroni.pomodorotime.utils;

import lombok.extern.slf4j.Slf4j;
import java.lang.management.ManagementFactory;

@Slf4j
public class JavaVMSpecsPrinter {

    public static final void printSpecs() {
        log.info("javaVersion: {}", System.getProperty("java.version"));
        log.info("javaVersionDate: {}", System.getProperty("java.version.date"));
        log.info("javaVmName: {}", System.getProperty("java.vm.name"));
        log.info("javaVendor: {}", System.getProperty("java.vendor"));
        log.info("vmVersion: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
    }
}
