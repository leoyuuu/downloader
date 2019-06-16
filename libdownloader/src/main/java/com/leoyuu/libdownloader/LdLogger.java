package com.leoyuu.libdownloader;

import android.support.annotation.Nullable;

/**
 * date 2019-06-16
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public interface LdLogger {
    /**
     * ld downloader log info
     * @param msg log msg
     * @param t error info
     */
    void onLog(String msg, @Nullable Throwable t);
}
