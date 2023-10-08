package com.example.testapplication.data.models.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.testapplication.data.models.entities.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {

    @Query("SELECT * FROM item")
    fun getAll(): Flow<List<Item>>

    @Upsert
    suspend fun upsertAll(vararg items: Item)

    @Query("SELECT * FROM item WHERE id = :itemId")
    fun getItemByTitle(itemId: Int): Item

    @Query("DELETE FROM item")
    fun purgeAll()
}