package com.abhi.logsbrologs.utils;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by Abhishek on 07-05-2017.
 */

public class Utils {

    public static boolean fileExist(String file) {
        String s = Shell.SU.run("[ -f \"" + file + "\" ] && echo true || echo false").get(0);
        return Boolean.parseBoolean(s);
    }
}
