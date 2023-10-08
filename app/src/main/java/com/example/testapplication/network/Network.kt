package com.example.testapplication.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


object Network {

    private lateinit var weakApp: WeakReference<Application>

    fun init(weakApp: WeakReference<Application>) {
        this.weakApp = weakApp
    }

    val service: ApiService by lazy {

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(weakApp)) // let it crash if not initialized
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.jsonkeeper.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }

    fun isNetworkAvailable(weakContext: WeakReference<Application>): Boolean {
        weakContext.get()?.let { context ->
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        } ?: kotlin.run {
            return false
        }
    }
}