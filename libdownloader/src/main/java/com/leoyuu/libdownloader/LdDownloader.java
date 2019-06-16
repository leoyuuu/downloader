package com.leoyuu.libdownloader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Copyright 2019-2024 leoyuuu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * date 2019-06-16
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public class LdDownloader {
    private static LdConfig config = null;

    public static void config(LdConfig config) {
        LdDownloader.config = config;
        ensureConfig();
    }

    public static void download(@NonNull String url, @NonNull String localPath, @Nullable Callback callback) {
        LdDownloadItem item = new LdDownloadItem().setUrl(url).setLocalPath(localPath).setCallback(callback).setConfig(ensureConfig());
        download(item);
    }

    public static void download(@NonNull LdDownloadItem downloadItem) {
        LdDownloaderImp.download(downloadItem);
    }

    private static LdConfig ensureConfig() {
        if (config == null || config.context == null) {
            throw new IllegalStateException("you should init the config with context first");
        }
        return config;
    }

    /**
     * download callback
     */
    public interface Callback {
        /**
         * invoked when download success
         * @param url the file url to download
         * @param localPath the file where to save
         */
        void onSuccess(@NonNull String url, @NonNull String localPath);

        /**
         * invoked when download failed
         * @param code error code
         * @param reason fail reason
         */
        void onFailed(int code, @NonNull String reason);


        /**
         * invoked when percent update
         * @param current current byte downloaded
         * @param total total byte need to be downloaded
         */
        void onProgress(long current, long total);
    }
}
