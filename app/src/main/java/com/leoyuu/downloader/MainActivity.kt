package com.leoyuu.downloader

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import com.leoyuu.libdownloader.LdConfig
import com.leoyuu.libdownloader.LdDownloader
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

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
 * date 2019-06-16
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class MainActivity : AppCompatActivity() {

    private var fileCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LdDownloader.config(LdConfig.configDefault(this))
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        download_btn.setOnClickListener {
            val url = url_et.text?.toString()
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileCount.toString())
            if (url == null) {
                return@setOnClickListener
            }
            LdDownloader.download(url, file.absolutePath, object : LdDownloader.Callback{
                override fun onSuccess(url: String, localPath: String) {
                    info_tv.text = "download success"
                }

                override fun onFailed(code: Int, reason: String) {
                    info_tv.text = "download failed: $reason"
                }

                override fun onProgress(current: Long, total: Long) {
                    info_tv.text = "download progress current: $current, total:$total"
                }
            })
        }
    }
}
