package com.app.shakealertla.Utils;

import android.util.Log;

/**
 * Created by Kamran Ahmed on 5/1/2015.
 */
public class AppLog {

    public static int v(String tag, String msg) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.v(tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.v(tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.d(tag, msg);
        }
        return 0;
    }


    public static int d(String tag, String msg, Throwable tr) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.d(tag, msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.i(tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.i(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.w(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.w(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, Throwable tr) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.w(tag, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.e(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (ConfigConstants.PRINT_LOGS) {
            return Log.e(tag, msg, tr);
        }
        return 0;
    }
}
