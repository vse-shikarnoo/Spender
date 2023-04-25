package com.example.spender.data

import com.google.firebase.FirebaseNetworkException

object DataErrorHandler {
    fun handle(error: Exception): DataResult<Nothing> {
        return when (error) {
            is FirebaseNetworkException -> {
                DataResult.Error("Network error")
            }
            else -> {
                DataResult.Error(error.message ?: "Unknown error")
            }
        }
    }
}
