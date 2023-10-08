package com.example.testapplication.data.models.entities

sealed class ApiResponse<T>

class Success<T>(val data: T): ApiResponse<T>()

class Failure<T>(val noInternet: Boolean = false, val error: Exception? = null): ApiResponse<T>()

object NoInternetException: Exception()

object NoDataException: Exception()

