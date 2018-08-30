package com.t9l.androidkotlindemo.retrofitDemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.t9l.androidkotlindemo.R
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RetrofitDemoActivity : AppCompatActivity() {

    private val mGitHubApi by lazy {
        GitHubApi.create()
    }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_demo)

        supportActionBar?.title = "Retrofit Demo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mGitHubApi.getUserRepos("Bob")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
    }

    private val mObserver: Observer<List<Repo>> = object : Observer<List<Repo>> {
        override fun onComplete() {
            logMessage("All done yah!")
        }

        override fun onSubscribe(d: Disposable) {
            logMessage("onSubscribe")
            disposable = d
        }

        override fun onNext(repos: List<Repo>) {
            logMessage("onNext")

            if (repos.isEmpty()) {
                logMessage("No repo found")
                return
            }

            // Do something with data
            logMessage(repos.toString())
        }

        override fun onError(exception: Throwable) {
            logMessage("Error: ${exception.localizedMessage}")
        }
    }

    private fun logMessage(message: String) {
        Log.d("RetrofitDemoActivity", message)
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

}
