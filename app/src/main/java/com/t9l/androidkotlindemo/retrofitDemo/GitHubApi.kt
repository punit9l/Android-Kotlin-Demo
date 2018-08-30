package com.t9l.androidkotlindemo.retrofitDemo

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


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