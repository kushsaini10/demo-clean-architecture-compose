package com.example.testapplication.network

import com.example.testapplication.data.models.dto.ItemsDto
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("b/WN0G")
    fun items(): Call<ItemsDto>
}
