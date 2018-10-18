package com.t9l.androidkotlindemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.t9l.androidkotlindemo.avd.AnimationActivity
import com.t9l.androidkotlindemo.customAlertDialog.CustomAlertActivity
import com.t9l.androidkotlindemo.recyclerView.RecyclerActivity
import com.t9l.androidkotlindemo.retrofitDemo.RetrofitDemoActivity
import com.t9l.androidkotlindemo.sensors.AccelerometerActivity
import com.t9l.androidkotlindemo.uberUX.UberUxActivity
import com.t9l.androidkotlindemo.userLocation.UserLocationActivity
import com.t9l.playernotification.NotificationService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val OVERLAY_PERMISSION_REQ = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            val intent = Intent(this, NotificationService::class.java)
            startService(intent)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NotificationService::class.java)
                startService(intent)
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_uber_ux -> {
                startActivity(Intent(this, UberUxActivity::class.java))
            }
            R.id.nav_avd -> {
                startActivity(Intent(this, AnimationActivity::class.java))
            }
            R.id.nav_retrofit_demo -> {
                startActivity(Intent(this, RetrofitDemoActivity::class.java))
            }
            R.id.nav_location -> {
                startActivity(Intent(this, UserLocationActivity::class.java))
            }
            R.id.nav_accelerometer -> {
                startActivity(Intent(this, AccelerometerActivity::class.java))
            }
            R.id.nav_custom_alert -> {
                startActivity(Intent(this, CustomAlertActivity::class.java))
            }
            R.id.nav_recycler_view -> {
                startActivity(Intent(this, RecyclerActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
