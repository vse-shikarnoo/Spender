package com.example.spender.data

sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: String) : DataResult<Nothing>()
}
