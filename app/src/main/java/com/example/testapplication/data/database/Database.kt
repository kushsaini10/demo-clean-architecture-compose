package com.example.testapplication.data.database

import androidx.room.Room
import com.example.testapplication.MyApplication
import java.lang.ref.WeakReference

object Database {

    lateinit var database: AppDatabase
        private set

    fun init(weakApp: WeakReference<MyApplication>) {
        database = Room.databaseBuilder(
            weakApp.get()!!,
            AppDatabase::class.java, "items-db"
        ).build()
    }
}