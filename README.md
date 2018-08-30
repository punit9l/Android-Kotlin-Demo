# Android Kotlin Demo



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

```kotlin
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