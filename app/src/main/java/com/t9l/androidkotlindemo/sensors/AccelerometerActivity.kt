package com.t9l.androidkotlindemo.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_accelerometer.*

class AccelerometerActivity : AppCompatActivity() {

    private lateinit var mSensorManager: SensorManager
    private val mAccelerometerReading = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerometer)

        supportActionBar?.title = "Accelerometer"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Register Accelerometer Sensor
        registerAccelerometer()
    }

    private fun registerAccelerometer() {
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            mSensorManager.registerListener(
                    mAccelerometerSensorListener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    private val mAccelerometerSensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            logMessage("onAccuracyChanged")
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.size)

                logMessage("${mAccelerometerReading[0]}, ${mAccelerometerReading[1]}, ${mAccelerometerReading[2]}")

                textViewAzimuth.text = getString(R.string.azimuth, mAccelerometerReading[0])
                textViewPitch.text = getString(R.string.pitch, mAccelerometerReading[1])
                textViewRoll.text = getString(R.string.roll, mAccelerometerReading[2])
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // Unregister sensor listener
        mSensorManager.unregisterListener(mAccelerometerSensorListener)
    }

    private fun logMessage(message: String) {
        Log.d("AccelerometerActivity", message)
    }
}
