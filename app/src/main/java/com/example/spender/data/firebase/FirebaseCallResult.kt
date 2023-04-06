package com.example.spender.data.firebase

sealed class FirebaseCallResult<out T> {
    data class Success<out T>(val data: T) : FirebaseCallResult<T>()
    data class Error(val exception: String) : FirebaseCallResult<Nothing>()
}
