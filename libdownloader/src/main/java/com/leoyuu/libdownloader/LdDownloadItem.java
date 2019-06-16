package com.leoyuu.libdownloader;

import java.io.File;

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
public class LdDownloadItem {
    String url;
    String localPath;
    LdDownloader.Callback callback;
    LdConfig config;

    private File cacheFile;

    public String getUrl() {
        return url;
    }

    public LdDownloadItem setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLocalPath() {
        return localPath;
    }

    public LdDownloadItem setLocalPath(String localPath) {
        this.localPath = localPath;
        return this;
    }

    public LdDownloader.Callback getCallback() {
        return callback;
    }

    public LdDownloadItem setCallback(LdDownloader.Callback callback) {
        this.callback = callback;
        return this;
    }

    LdDownloadItem setConfig(LdConfig config) {
        this.config = config;
        return this;
    }

    File getCacheFile() {
        if (cacheFile == null) {
            File parent = new File(localPath).getParentFile();
            cacheFile = new File(parent, String.valueOf(url.hashCode()) + localPath.hashCode() + ".cache");
        }
        return cacheFile;
    }
}
