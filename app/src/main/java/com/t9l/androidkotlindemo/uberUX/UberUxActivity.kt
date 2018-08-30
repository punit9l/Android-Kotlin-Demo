package com.t9l.androidkotlindemo.uberUX

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.uber_ux_main_content.*

class UberUxActivity : AppCompatActivity() {

    companion object {
        private const val SCREEN_TITLE = "UBER UX"
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uber_ux)

        supportActionBar?.title = SCREEN_TITLE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Just to simulate Uber UI
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)


        // BottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, scrollPosition: Float) {
                animateContentView(scrollPosition)
            }

            override fun onStateChanged(p0: View, state: Int) {
                bottomSheetStateChanged(state)
            }
        })

        // BottomSheet title
        textViewTitle.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        buttonCloseBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    private fun animateContentView(scrollPosition: Float) {
        val scaleFactor = 1.0 - scrollPosition  // reverse the value
        // logMessage("$scaleFactor")

        val alphaScaleFactor = scaleFactor * 1.0
        // logMessage("$alphaScaleFactor")

        locationView.alpha = alphaScaleFactor.toFloat()
        locationView.scaleX = scaleFactor.toFloat()
        locationView.scaleY = scaleFactor.toFloat()
    }

    private fun bottomSheetStateChanged(state: Int) {
        when (state) {

            BottomSheetBehavior.STATE_EXPANDED -> {
                // logMessage("STATE_EXPANDED")
                supportActionBar?.title = "Booking Details"
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
                window.statusBarColor = Color.BLACK

                textViewTitle.visibility = View.INVISIBLE
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                // logMessage("STATE_COLLAPSED")
                supportActionBar?.title = SCREEN_TITLE
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

                val typedValuePrimary = TypedValue()
                val typedValuePrimaryDark = TypedValue()
                theme.resolveAttribute(R.attr.colorPrimary, typedValuePrimary, true)
                theme.resolveAttribute(R.attr.colorPrimaryDark, typedValuePrimaryDark, true)
                val colorPrimary = typedValuePrimary.data
                val colorPrimaryDark = typedValuePrimaryDark.data

                supportActionBar?.setBackgroundDrawable(ColorDrawable(colorPrimary))
                window.statusBarColor = colorPrimaryDark

                textViewTitle.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    return true
                } else {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        } else {
            super.onBackPressed()
        }
    }
}
