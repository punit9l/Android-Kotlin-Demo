package com.t9l.playernotification

import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.t9l.playernotification.Constant.ACTION_KEY
import com.t9l.playernotification.Constant.ACTION_NEXT
import com.t9l.playernotification.Constant.ACTION_PAUSE
import com.t9l.playernotification.Constant.ACTION_PLAY
import com.t9l.playernotification.Constant.ACTION_PREV
import com.t9l.playernotification.Constant.ACTION_STOP
import com.t9l.playernotification.Constant.PLAYER_ACTION


class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotificationUtils.createMusicNotification(this, false, "Title", "Hello")
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        logMessage("onCreate")

        // Register intent action broadcast receiver
        val broadcastIntentFilter = IntentFilter()
        broadcastIntentFilter.addAction(PLAYER_ACTION)
        registerReceiver(mIntentBroadcastReceiver, broadcastIntentFilter)
    }

    private val mIntentBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logMessage("onReceive")

            when (intent.getIntExtra(ACTION_KEY, 0)) {
                ACTION_PREV -> {
                    // TODO
                    logMessage("ACTION_PREV")
                    return
                }
                ACTION_PLAY -> {
                    // TODO
                    logMessage("ACTION_PLAY")
                    NotificationUtils.createMusicNotification(context, true, "Track Name", "Artist Name")
                    return
                }
                ACTION_PAUSE -> {
                    // TODO
                    logMessage("ACTION_PAUSE")
                    NotificationUtils.createMusicNotification(context, false, "Track Name", "Artist Name")
                    return
                }
                ACTION_NEXT -> {
                    // TODO
                    logMessage("ACTION_NEXT")
                    return
                }
                ACTION_STOP -> {
                    // TODO
                    logMessage("ACTION_STOP")
                    stopSelf()
                    return
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logMessage("onDestroy")
        // Unregister broadcast receiver
        unregisterReceiver(mIntentBroadcastReceiver)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        stopSelf()
    }

    private fun logMessage(message: String) {
        Log.i("NotificationService", message)
    }
}
