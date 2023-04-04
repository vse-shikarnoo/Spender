package com.example.spender.data.firebase

import com.google.firebase.FirebaseNetworkException

object HandleError {
    fun handleFirebaseError(error: Throwable): Result<Nothing> {
        return when (error) {
            is FirebaseNetworkException -> {
                Result.Error("Network error")
            }
            else -> {
                Result.Error(error.message ?: "Unknown error")
            }
        }
    }
}
