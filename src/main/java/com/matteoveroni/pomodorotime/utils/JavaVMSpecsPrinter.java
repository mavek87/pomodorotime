package com.matteoveroni.pomodorotime.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;

public class JavaVMSpecsPrinter {

    private static final Logger log = LoggerFactory.getLogger(JavaVMSpecsPrinter.class);

    public static final void printSpecs() {
        log.info("javaVersion: {}", System.getProperty("java.version"));
        log.info("javaVersionDate: {}", System.getProperty("java.version.date"));
        log.info("javaVmName: {}", System.getProperty("java.vm.name"));
        log.info("javaVendor: {}", System.getProperty("java.vendor"));
        log.info("vmVersion: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
    }
}
