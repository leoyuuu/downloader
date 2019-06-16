package com.leoyuu.libdownloader;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * date 2019-06-16
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public class LdConfig implements RejectedExecutionHandler {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = 0;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    Application context;
    LdLogger logger = null;
    Executor downloadExecutor = null;
    Executor callbackExecutor = null;


    public static LdConfig configDefault(Context context) {
        LdConfig ldConfig = new LdConfig();
        ldConfig.context = (Application) context.getApplicationContext();
        ldConfig.downloadExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), new DownloadThreadFactory(), ldConfig);
        ldConfig.logger = new LdLogger() {
            @Override
            public void onLog(String msg, @Nullable Throwable t) {
                if (BuildConfig.DEBUG) {
                    android.util.Log.d("LdDownloader", msg, t);
                }
            }
        };
        ldConfig.callbackExecutor = new Executor() {
            private Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void execute(@NonNull Runnable command) {
                handler.post(command);
            }
        };
        return ldConfig;
    }

    private static class DownloadThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "LdDownloader: " + threadNumber.getAndIncrement());
        }
    }


    private final AtomicInteger ignoreCount = new AtomicInteger(0);
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (logger != null) {
            logger.onLog("download task queue full, ignore task: " + r + ", ignore count: " + ignoreCount.incrementAndGet(), null);
        }
    }

}
