package com.example.testapplication.ui.state

import com.example.testapplication.data.models.entities.Item

sealed class UiState

object Loading : UiState()
class Loaded(val data: List<Item>): UiState()
class Error(val noInternet: Boolean = false, val message: String?): UiState()