package com.example.testapplication.repository

import android.app.Application
import com.example.testapplication.data.database.Database
import com.example.testapplication.data.models.entities.ApiResponse
import com.example.testapplication.data.models.entities.Failure
import com.example.testapplication.data.models.entities.Item
import com.example.testapplication.data.models.entities.NoDataException
import com.example.testapplication.data.models.entities.Success
import com.example.testapplication.network.ApiService
import com.example.testapplication.network.Network
import com.example.testapplication.usecase.ParseItemsUsecase
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import java.lang.ref.WeakReference

/**
 * injected [ApiService] in constructor so mocking is easier for testing
 */
class Repository(private val weakApp: WeakReference<Application>, private val service: ApiService) {

    suspend fun getItems(): ApiResponse<List<Item>> {
        return if (Network.isNetworkAvailable(weakApp).not()) {
            // load from db
            loadFromDb()
        } else {
            // try to hit network
            val apiResponse = loadFromNetwork()

            if (apiResponse is Failure) {

                // load from db
                val dbResponse = loadFromDb()
                if (dbResponse is Failure) {
                    apiResponse // return failure response
                } else {
                    dbResponse
                }
            } else {
                apiResponse
            }
        }
    }

    private suspend fun loadFromNetwork(): ApiResponse<List<Item>> {
        return try {
            val response = service.items().execute()
            val apiResponse = ParseItemsUsecase().execute(response)
            updateDatabase(apiResponse)
            if (apiResponse is Success) {
                loadFromDb() // we need auto-generated primary key ids for navigation
            } else {
                apiResponse
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(false, e)
        }
    }

    private suspend fun updateDatabase(apiResponse: ApiResponse<List<Item>>) {
        if (apiResponse is Success) {
                Database.database.itemsDao().purgeAll()
            Database.database.itemsDao().upsertAll(*apiResponse.data.toTypedArray())
        }
    }

    private suspend fun loadFromDb(): ApiResponse<List<Item>> {
        val items = Database.database.itemsDao().getAll().take(1).single()
        return if (items.isNotEmpty()) {
            Success(items)
        } else {
            Failure(error = NoDataException)
        }
    }

    suspend fun getItem(itemId: Int): Item {
        return Database.database.itemsDao().getItemByTitle(itemId)
    }
}
