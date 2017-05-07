package com.abhi.logsbrologs;

public class Constants {
    public enum LogLevel {LOGLEVEL_I, LOGLEVEL_V, LOGLEVEL_W, LOGLEVEL_D, LOGLEVEL_E, LOGLEVEL_F,
        LOGLEVEL_DENIALS, LOGLEVEL_UNDEFINED}

    public static String RAMOOPS = "/sys/fs/pstore/console-ramoops";
    public static String LAST_KMSG = "/proc/last_kmsg";
    public static String KMSG = "/proc/kmsg";
}
