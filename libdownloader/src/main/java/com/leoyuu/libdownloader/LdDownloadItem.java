package com.leoyuu.libdownloader;

import java.io.File;

/**
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
