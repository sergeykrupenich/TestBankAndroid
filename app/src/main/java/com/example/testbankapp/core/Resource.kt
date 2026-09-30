package com.example.testbankapp.core

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> {
            val rawData = data
            if (rawData != null) {
                Resource.Success(transform(rawData))
            } else {
                Resource.Error("Data is null")
            }
        }
        is Resource.Error -> Resource.Error(message ?: "Unknown error")
        is Resource.Loading -> Resource.Loading()
    }
}
