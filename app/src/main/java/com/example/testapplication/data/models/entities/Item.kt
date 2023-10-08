package com.example.testapplication.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(

    @PrimaryKey
    val title: String,

    val desc: String,

    val date: String,

    val image: String?
)
