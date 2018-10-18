package com.t9l.androidkotlindemo.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.view.Gravity
import android.R.attr.gravity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import com.t9l.androidkotlindemo.R


class MyService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var params: WindowManager.LayoutParams

    private val mWindowType: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        @Suppress("DEPRECATION")
        WindowManager.LayoutParams.TYPE_PHONE
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // show the alert her
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        // use your custom view here
        floatingView = View.inflate(baseContext, R.layout.floating_layout, null)

        params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                mWindowType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT)

        params.gravity = Gravity.CENTER or Gravity.CENTER
        // add the view to window manger
        windowManager.addView(floatingView, params)
        return Service.START_STICKY
    }
}
