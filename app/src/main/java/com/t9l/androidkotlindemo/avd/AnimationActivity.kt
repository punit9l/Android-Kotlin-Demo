package com.t9l.androidkotlindemo.avd

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        supportActionBar?.title = "Animation"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawable = imageViewHeart.drawable

        val avd = drawable as AnimatedVectorDrawable

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            avd.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    avd.start()
                }
            })
        }

        avd.start()
    }
}
