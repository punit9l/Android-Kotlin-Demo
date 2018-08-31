package com.t9l.androidkotlindemo.customAlertDialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_custom_alert.*
import kotlinx.android.synthetic.main.alert_dialog.view.*

class CustomAlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_alert)

        supportActionBar?.title = "Custom Alert Dialog"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonAlert.setOnClickListener {
            val dialog = createCustomDialog(this)
            dialog.show()
        }
    }

    private fun createCustomDialog(context: Context): AlertDialog {

        val builder = AlertDialog.Builder(context)
        // Get the layout inflater
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.alert_dialog, null)
        // Inflate and set the layout for the dialog
        builder.setView(view)

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        view.imageButton.setOnClickListener { dialog.dismiss() }
        return dialog
    }
}
