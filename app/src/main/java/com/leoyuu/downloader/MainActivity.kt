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
