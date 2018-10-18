package com.t9l.playernotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.t9l.playernotification.Constant.ACTION_KEY
import com.t9l.playernotification.Constant.ACTION_NEXT
import com.t9l.playernotification.Constant.ACTION_PAUSE
import com.t9l.playernotification.Constant.ACTION_PLAY
import com.t9l.playernotification.Constant.ACTION_PREV
import com.t9l.playernotification.Constant.ACTION_STOP
import com.t9l.playernotification.Constant.PLAYER_ACTION


object NotificationUtils {

    private const val TAG = "NOTIFICATION_TAG"
    private const val PRIMARY_CHANNEL = "video_player"
    private const val PRIMARY_CHANNEL_NAME = "Video Player"

    private const val NOTIFICATION_ID = 3

    // Broadcast sender request code
    private const val BROADCAST_REQ_CODE_PREV = 101
    private const val BROADCAST_REQ_CODE_PLAY = 102
    private const val BROADCAST_REQ_CODE_PAUSE = 103
    private const val BROADCAST_REQ_CODE_NEXT = 104
    private const val BROADCAST_REQ_CODE_STOP = 105

    fun createMusicNotification(context: Context, isPlaying: Boolean, title: String, body: String) {
        logMessage("createMusicNotification")

        //Open the application  click.
        val intent = Intent(context, CurrentlyPlayingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val play = Intent(PLAYER_ACTION)
        play.putExtra(ACTION_KEY, ACTION_PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(context, BROADCAST_REQ_CODE_PREV, play, PendingIntent.FLAG_UPDATE_CURRENT)

        val pause = Intent(PLAYER_ACTION)
        pause.putExtra(ACTION_KEY, ACTION_PAUSE)
        val pausePendingIntent = PendingIntent.getBroadcast(context, BROADCAST_REQ_CODE_PLAY, pause, PendingIntent.FLAG_UPDATE_CURRENT)

        val next = Intent(PLAYER_ACTION)
        next.putExtra(ACTION_KEY, ACTION_NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(context, BROADCAST_REQ_CODE_PAUSE, next, PendingIntent.FLAG_UPDATE_CURRENT)

        val prev = Intent(PLAYER_ACTION)
        prev.putExtra(ACTION_KEY, ACTION_PREV)
        val prevPendingIntent = PendingIntent.getBroadcast(context, BROADCAST_REQ_CODE_NEXT, prev, PendingIntent.FLAG_UPDATE_CURRENT)

        val stop = Intent(PLAYER_ACTION)
        stop.putExtra(ACTION_KEY, ACTION_STOP)
        val stopPendingIntent = PendingIntent.getBroadcast(context, BROADCAST_REQ_CODE_STOP, stop, PendingIntent.FLAG_UPDATE_CURRENT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            logMessage("Build.VERSION.SDK_INT >= Build.VERSION_CODES.O")
            val notificationBuilder = Notification.Builder(context, PRIMARY_CHANNEL)
                    .setSmallIcon(Icon.createWithResource(context, R.drawable.music_notes))
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(!isPlaying)
                    .setOngoing(isPlaying)

            // Notifications in Android N
            // Doesn't display action icons to give more space for actions
            // https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html

            val prevAction = Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_skip_previous_black_24dp), "Prev", prevPendingIntent).build()
            val playAction = Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_play_arrow_black_24dp), "Play", playPendingIntent).build()
            val pauseAction = Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_pause_black_24dp), "Pause", pausePendingIntent).build()
            val nextAction = Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_skip_next_black_24dp), "Next", nextPendingIntent).build()

            notificationBuilder.addAction(prevAction)
                    .addAction(if (isPlaying) pauseAction else playAction)
                    .addAction(nextAction)
                    .setDeleteIntent(stopPendingIntent)

            val channel = createChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME)
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        } else {
            logMessage("Build.VERSION.SDK_INT <= Build.VERSION_CODES.O")

            val mBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                    .setSmallIcon(R.drawable.music_notes)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(!isPlaying)
                    .setOngoing(isPlaying)

            val prevAction = NotificationCompat.Action.Builder(R.drawable.ic_skip_previous_black_24dp, "Prev", prevPendingIntent).build()
            val playAction = NotificationCompat.Action.Builder(R.drawable.ic_play_arrow_black_24dp, "Play", playPendingIntent).build()
            val pauseAction = NotificationCompat.Action.Builder(R.drawable.ic_pause_black_24dp, "Pause", pausePendingIntent).build()
            val nextAction = NotificationCompat.Action.Builder(R.drawable.ic_skip_next_black_24dp, "Next", nextPendingIntent).build()

            mBuilder.addAction(prevAction)
                    .addAction(if (isPlaying) pauseAction else playAction)
                    .addAction(nextAction)
                    .setDeleteIntent(stopPendingIntent)

            notificationManager.notify(NOTIFICATION_ID, mBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channel: String, channelName: String): NotificationChannel {
        return NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.GREEN
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            setSound(null, null)
            description = "Show when playing video"
        }
    }

    private fun logMessage(message: String) {
        Log.i(TAG, message)
    }
}