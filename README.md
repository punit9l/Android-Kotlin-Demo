# Android Kotlin Demo

### Table of Contents
---
1. [UBER UX](https://github.com/punit9l/Android-Kotlin-Demo#1-uber-ux)
2. [Animation](https://github.com/punit9l/Android-Kotlin-Demo#2-animation)
3. [Retrofit Demo](https://github.com/punit9l/Android-Kotlin-Demo#3-retrofit-demo)
4. [Location Demo](https://github.com/punit9l/Android-Kotlin-Demo#4-location-demo)

---

### 1. UBER UX

See code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/uberUX)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/uber_ux.gif" width=300></img>

---

### 2. Animation

See code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/avd)

<img src="https://github.com/punit9l/Android-Kotlin-Demo/raw/master/screen_shots/heart_loading.gif" width=300></img>

---

### 3. Retrofit Demo

See code [here](https://github.com/punit9l/Android-Kotlin-Demo/tree/master/app/src/main/java/com/t9l/androidkotlindemo/retrofitDemo)


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

