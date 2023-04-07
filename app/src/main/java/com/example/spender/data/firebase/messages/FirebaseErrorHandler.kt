package com.example.spender.data.firebase.messages

import com.example.spender.data.firebase.FirebaseCallResult
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestoreException

object FirebaseErrorHandler {
    fun handle(error: Exception): FirebaseCallResult<Nothing> {
        return when (error) {
            is FirebaseNetworkException -> {
                FirebaseCallResult.Error("Network error")
            }
            else -> {
                FirebaseCallResult.Error(error.message ?: "Unknown error")
            }
        }
    }
}
