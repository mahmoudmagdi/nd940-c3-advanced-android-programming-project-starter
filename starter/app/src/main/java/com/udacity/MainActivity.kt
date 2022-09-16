package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.DownloadManager.*
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.apache.commons.io.FilenameUtils

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private var fileUrl = ""
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager

        //Create a notification channel
        NotificationHelper.createChannel(CHANNEL_ID, CHANNEL_NAME, notificationManager)

        //Register the Broadcast Receiver to alert the application when the file is downloaded
        registerReceiver(receiver, IntentFilter(ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            Thread {
                download()
            }.start()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                NotificationHelper.buildNotification(
                    this@MainActivity,
                    resources.getString(R.string.notification_description),
                    fileUrl,
                    fileName
                )
            }
        }
    }

    @SuppressLint("Range")
    private fun download() {
        if (downloading_options_radio.checkedRadioButtonId == -1) {
            Toast.makeText(
                this,
                "Please select one option to continue downloading...",
                Toast.LENGTH_LONG
            ).show()
            custom_button.changeButtonState(ButtonState.Completed)
        } else {
            fileUrl =
                findViewById<RadioButton>(downloading_options_radio.checkedRadioButtonId).text.toString()
            fileName = FilenameUtils.getName(fileUrl)
            Log.d("selectedUrl", fileUrl)
            Log.d("fileName", fileName)

            val request =
                Request(Uri.parse(fileUrl))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }

    companion object {
        const val CHANNEL_ID = "channelId"
        const val CHANNEL_NAME = "channelName"
    }
}
