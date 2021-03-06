# Android Kotlin Demo

### Table of Contents
---
1. [UBER UX](https://github.com/punit9l/Android-Kotlin-Demo#1-uber-ux)
2. [Animation](https://github.com/punit9l/Android-Kotlin-Demo#2-animation)
3. [Retrofit Demo](https://github.com/punit9l/Android-Kotlin-Demo#3-retrofit-demo)
4. [Location Demo](https://github.com/punit9l/Android-Kotlin-Demo#4-location-demo)
5. [Accelerometer Demo](https://github.com/punit9l/Android-Kotlin-Demo#5-accelerometer-demo)
6. [Custom Alert Dialog](https://github.com/punit9l/Android-Kotlin-Demo#6-custom-alert-dialog)
7. [RecyclerView Demo](https://github.com/punit9l/Android-Kotlin-Demo#7-recyclerview-demo)

---

### 1. UBER UX

See full code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/uberUX)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/uber_ux.gif" width=300></img>

[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)

---

### 2. Animation

See full code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/avd)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/heart_loading.gif" width=300></img>

[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)

---

### 3. Retrofit Demo

See full code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/retrofitDemo)


build.graddle (app)

```graddle
// Retrofit
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
implementation "com.squareup.retrofit2:retrofit:2.4.0"
implementation "com.squareup.retrofit2:adapter-rxjava2:2.4.0"
implementation "io.reactivex.rxjava2:rxandroid:2.0.2"
```
Repo.kt

```kotlin
data class Repo(
        val id: Int,
        val name: String,
        val private: Boolean,
        val language: String
)
```

GitHubApi.kt

```kotlin
interface GitHubApi {

    @GET("users/{user}/repos")
    fun getUserRepos(@Path("user") user: String): Observable<List<Repo>>

    companion object {
        fun create(): GitHubApi {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.github.com")
                    .build()

            return retrofit.create(GitHubApi::class.java)
        }
    }
}
```

RetrofitDemoActivity.kt

```kotlin
class RetrofitDemoActivity : AppCompatActivity() {

    private val mGitHubApi by lazy {
        GitHubApi.create()
    }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_demo)

     	// ...

        mGitHubApi.getUserRepos("Bob")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
    }

    private val mObserver: Observer<List<Repo>> = object : Observer<List<Repo>> {
    	override fun onSubscribe(d: Disposable) {
            disposable = d
        }
        // more override methods
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }
}
```

[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)

---

### 4. Location Demo

See code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/userLocation)

build.graddle (app)

```graddle
implementation "com.google.android.gms:play-services-location:15.0.1"
```

AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

UserLocationActivity.kt

```kotlin
class UserLocationActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_REQUEST = 2018
    }
    private lateinit var mLocationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        
        // ...

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!isLocationEnabled()) {
            // TODO
            Toast.makeText(this, "Location is disabled!", Toast.LENGTH_LONG).show()
        }
        // get last known location if possible or request
        requestLocation()
    }

    private fun requestLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)
        } else {
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
        // Do something cool
    }


    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            foundLocation(location!!)
        }

        override fun onProviderEnabled(p0: String?) {
            requestLocation()
        }
        
        // ...
        // more overide
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    requestLocation()
                } else {
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
        // ...
        mLocationManager.removeUpdates(mLocationListener)
    }
}
```

[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)

---

### 5. Accelerometer Demo

See full code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/sensors)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/accelerometer.gif" width=300></img>

AccelerometerActivity.kt

```kotlin
class AccelerometerActivity : AppCompatActivity() {

    private lateinit var mSensorManager: SensorManager
    private val mAccelerometerReading = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        
        // ...

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
        
        // ...
        // overrides

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.size)
                // Do something with mAccelerometerReading
                // TODO
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // Unregister sensor listener
        mSensorManager.unregisterListener(mAccelerometerSensorListener)
    }
}
```
---

[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)


---

### 6. Custom Alert Dialog

See full code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/customAlertDialog)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/custom_dialog.png" width=300></img>

rounded.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <corners android:radius="15dp" />
    <solid android:color="#FFFFFF" />
</shape>
```

alert_dialog.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="32dp"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_marginLeft="@dimen/fab_margin"
        android:elevation="1dp"
        app:srcCompat="@drawable/quotes" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="-30dp"
        android:background="@drawable/rounded"
        android:elevation="0dp"
        android:padding="32dp"
        android:text="@string/dummy_text"
        android:textSize="20sp" />

    <ImageView
        android:id="@+id/imageButton"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_gravity="center"
        android:layout_marginTop="20dp"
        android:adjustViewBounds="false"
        android:cropToPadding="false"
        app:srcCompat="@drawable/cross_copy" />
</LinearLayout>
```

CustomAlertActivity.kt

```kotlin
class CustomAlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        
        // ...

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
```

[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)

---

### 7. RecyclerView Demo

See full code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/recyclerView)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/recycler_view.png" width=300></img>

custom_item.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/textViewItem"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:padding="@dimen/fab_margin"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</android.support.constraint.ConstraintLayout>
```

CustomItem.kt

```kotlin
class CustomItem (
        val id: Int,
        val name: String
)
```

CustomAdapter.kt

```kotlin
class CustomAdapter(
        private val mutableList: MutableList<CustomItem>
): RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.custom_item, viewGroup, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItem(mutableList[position])
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: CustomItem) {
            itemView.textViewItem.text = item.name
        }

    }
}
```

RecyclerActivity.kt

```kotlin
class RecyclerActivity : AppCompatActivity() {

    private lateinit var mAdapter: CustomAdapter
    private val mutableList: MutableList<CustomItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        
        // ...

        mAdapter = CustomAdapter(mutableList)

        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()

        for (index in 1..100) {
            mutableList.add(CustomItem(index, "Item $index"))
            mAdapter.notifyDataSetChanged()
        }
    }
}
```
[Back to TOC](https://github.com/punit9l/Android-Kotlin-Demo#table-of-contents)

---
