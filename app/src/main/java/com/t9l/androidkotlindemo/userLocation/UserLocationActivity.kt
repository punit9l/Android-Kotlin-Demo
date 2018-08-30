package com.t9l.androidkotlindemo.userLocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_user_location.*

class UserLocationActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST = 2018
    }

    // Location
    private lateinit var mLocationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location)

        supportActionBar?.title = "User Location"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Location
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // Request for user location

        if (!isLocationEnabled()) {
            // TODO
            // Show feedback
            Toast.makeText(this, "Location is disabled!", Toast.LENGTH_LONG).show()
        }
        // get last known location if possible or request
        requestLocation()
    }

    /*
    * Location
    * */
    private fun requestLocation() {
        logMessage("requestLocation")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            logMessage("requestPermissions")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)
        } else {
            logMessage("do gps")

            fusedLocationClient.lastLocation
                    .addOnSuccessListener { lastKnownLocation: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (lastKnownLocation != null) foundLocation(lastKnownLocation)
                    }

            //   gps provider
            val locationGpsProvider = LocationManager.GPS_PROVIDER
            mLocationManager.requestLocationUpdates(locationGpsProvider, 1000, 0.1f, mLocationListener)
        }
    }


    private fun foundLocation(location: Location) {
        logMessage("Last known location, ${location.latitude} ${location.longitude}")
        textViewLatitude.text = getString(R.string.latitude, location.latitude)
        textViewLongitude.text = getString(R.string.longitude, location.longitude)
    }


    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            logMessage("onLocationChanged $location")
            foundLocation(location!!)
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            logMessage("onStatusChanged")
        }

        override fun onProviderEnabled(p0: String?) {
            logMessage("onProviderEnabled")
            requestLocation()
        }

        override fun onProviderDisabled(p0: String?) {
            logMessage("onProviderDisabled")
            // TODO goto setting
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                logMessage("LOCATION_REQUEST")
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    logMessage("permission was granted, yay!")
                    requestLocation()
                } else {
                    logMessage("permission denied, boo!")
                    // permission denied, boo!
                }
                return
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationMode: Int

        try {
            locationMode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    override fun onPause() {
        super.onPause()
        logMessage("onPause")

        // Location
        mLocationManager.removeUpdates(mLocationListener)
    }

    private fun logMessage(message: String) {
        Log.d("LocationActivity", message)
    }
}
