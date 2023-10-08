package com.example.testapplication.data.models.dto

import com.example.testapplication.data.Validatable
import com.google.gson.annotations.SerializedName

data class ItemDto(
    @SerializedName("title", alternate = ["Title"])
    val title: String?,

    @SerializedName("description")
    val desc: String?,

    val date: String?,

    @SerializedName("img")
    val image: String?
) : Validatable {

    override fun isValid(): Boolean {
        return title.isNullOrBlank().not()
    }
}
