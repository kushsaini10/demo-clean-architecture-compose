package com.example.testapplication

import android.app.Application
import com.example.testapplication.data.database.Database
import com.example.testapplication.network.Network
import com.jakewharton.threetenabp.AndroidThreeTen
import java.lang.ref.WeakReference

class MyApplication: Application() {

    override fun onCreate() {
        AndroidThreeTen.init(this)
        Database.init(WeakReference(this))
        Network.init(WeakReference(this))
        super.onCreate()
    }
}