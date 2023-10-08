package com.example.testapplication.network

import android.app.Application
import com.example.testapplication.data.models.entities.NoInternetException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.ref.WeakReference

class NetworkConnectionInterceptor(private val weakApp: WeakReference<Application>): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Network.isNetworkAvailable(weakApp)) {
            throw NoInternetException
        }

        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}