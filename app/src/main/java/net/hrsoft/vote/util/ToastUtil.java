/*
 * Copyright (c) 2017. www.hrsoft.net  Inc. All rights reserved.
 */

package net.hrsoft.vote.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Toast 工具类
 *
 * @author yuanzeng
 * @since 17/1/22 下午10:06
 */

public final class ToastUtil {
    private static final int duration = Toast.LENGTH_SHORT;
    private static final boolean isShowErrorCode = true;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, duration).show();
    }

    public static void showToast(Context context, @StringRes int resId) {

        Toast.makeText(context, resId, duration).show();
    }

    public static void showToast(Context context, String msg, int... errorCode) {
        if (isShowErrorCode) {
            for (int anErrorCode : errorCode) {
                msg = msg + "-" + anErrorCode;
            }
        }
        showToast(context, msg);
    }

    public static void showToast(Context context, @StringRes int resId, int... errorCode) {
        String msg = context.getResources().getString(resId);
        if (isShowErrorCode) {
            for (int anErrorCode : errorCode) {
                msg = msg + "-" + anErrorCode;
            }
        }
        showToast(context, msg);
    }
}
