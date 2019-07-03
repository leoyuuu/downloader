package com.leoyuu.libdownloader;

import android.support.annotation.NonNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
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
class LdDownloaderImp {

    static void download(@NonNull final LdDownloadItem downloadItem) {
        checkArguments(downloadItem);
        File localFile = new File(downloadItem.localPath);
        if (localFile.exists() && localFile.length() > 0) {
            if (downloadItem.callback != null) {
                downloadItem.callback.onSuccess(downloadItem.url, downloadItem.localPath);
            }
            if (downloadItem.config.logger != null) {
                downloadItem.config.logger.onLog("local file(" + downloadItem.localPath + ") already exist", null);
            }
        } else {
            downloadItem.config.downloadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    doDownload(downloadItem);
                }
            });
        }
    }

    private static void doDownload(LdDownloadItem item) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(item.getUrl()).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int rspCode = connection.getResponseCode();
            if (connection.getErrorStream() != null || rspCode != HttpURLConnection.HTTP_OK) {
                StringBuilder error = new StringBuilder();
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String line;
                    while ((line = r.readLine()) != null) {
                        error.append(line).append('\n');
                    }
                }
                invokeFail(item, rspCode,"Download failed: " + error);
                connection.disconnect();
                return;
            }

            File temp = item.getCacheFile();
            File parent = temp.getParentFile();
            if (parent == null) {
                invokeFail(item, -1, "file " + item.getLocalPath() + " not support");
                return;
            }
            if (!parent.exists()) {
                if (!parent.mkdirs()) {
                    invokeFail(item, -1, "create parent file failed");
                    return;
                }
            }
            InputStream stream = connection.getInputStream();
            try {
                OutputStream output = new FileOutputStream(temp);
                try {
                    byte[] buffer = new byte[1024];
                    int read;

                    int totalLen = connection.getContentLength();
                    int currentLen = 0;
                    int currentPercent = 0;

                    while ((read = stream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                        currentLen+=read;

                        int percent = currentLen * 100 / totalLen;
                        if (currentPercent != percent) {
                            currentPercent = percent;
                            invokeProgress(item, currentLen, totalLen);
                        }
                    }
                    output.flush();
                } finally {
                    output.close();
                }
                try {
                    if (temp.renameTo(new File(item.localPath))) {
                        invokeSuccess(item);
                    } else {
                        invokeFail(item, -1, "save file failed");
                    }
                } catch (SecurityException e) {
                    invokeFail(item, -1, e.getLocalizedMessage());
                }
            } finally {
                stream.close();
                connection.disconnect();
            }

        } catch (Exception e) {
            invokeFail(item, -1, e.getLocalizedMessage());
        }
    }

    private static void invokeFail(final LdDownloadItem item, final int code, final String reason) {
        if (item.callback != null) {
            item.config.callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    item.callback.onFailed(code, reason);
                }
            });
        }
    }

    private static void invokeSuccess(final LdDownloadItem item) {
        if (item.callback != null) {
            item.config.callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    item.callback.onSuccess(item.url, item.localPath);
                }
            });
        }
    }

    private static void invokeProgress(final LdDownloadItem item, final long cur, final long total) {
        if (item.callback != null) {
            item.config.callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    item.callback.onProgress(cur, total);
                }
            });
        }
    }

    private static void checkArguments(LdDownloadItem item) {
        if (item.config == null) {
            throw new IllegalArgumentException("ld download config null");
        }
        if (item.url == null) {
            throw new IllegalArgumentException("ld item url null");
        }

        if (item.localPath == null) {
            throw new IllegalArgumentException("ld item local path null");
        }
    }
}
