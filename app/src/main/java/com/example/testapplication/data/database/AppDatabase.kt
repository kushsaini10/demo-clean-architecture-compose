package com.example.testapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapplication.data.models.dao.ItemsDao
import com.example.testapplication.data.models.entities.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemsDao
}
