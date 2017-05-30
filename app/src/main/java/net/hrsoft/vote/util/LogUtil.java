/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.util;

import com.orhanobut.logger.Logger;

import net.hrsoft.vote.VoteApplication;

/**
 * Log 工具类
 *
 * @author yuanzeng
 * @since 17/1/22 下午4:57
 */

public final class LogUtil {

    private static final boolean DEBUG = VoteApplication.DEBUG;

    public static void d(String tag, String msg, Object... args) {
        if (DEBUG) {
            Logger.t(tag).d(msg, args);
        }
    }

    public static void d(String tag, Object args) {
        if (DEBUG) {
            Logger.t(tag).d(args);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        if (DEBUG) {
            Logger.t(tag).e(msg, args);
        }
    }

    public static void e(String tag, Throwable throwable, String message, Object... args) {
        if (DEBUG) {
            Logger.t(tag).e(throwable, message, args);
        }
    }

    public static void json(String tag, String json) {
        if (DEBUG) {
            Logger.t(tag).json(json);
        }
    }

    public static void xml(String tag, String xml) {
        if (DEBUG) {
            Logger.t(tag).xml(xml);
        }
    }

    public static void log(int priority, String tag, String message, Throwable throwable) {
        if (DEBUG) {
            Logger.log(priority, tag, message, throwable);
        }
    }

}
