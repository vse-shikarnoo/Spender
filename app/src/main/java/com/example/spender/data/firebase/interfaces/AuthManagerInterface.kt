package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.google.firebase.auth.FirebaseUser

interface AuthManagerInterface {
    suspend fun signIn(email: String, password: String): FirebaseCallResult<String>
    suspend fun signUp(email: String, password: String): FirebaseCallResult<FirebaseUser>
    suspend fun signOut(): FirebaseCallResult<String>
    suspend fun verifyEmail(): FirebaseCallResult<String>
    suspend fun isEmailVerified(): FirebaseCallResult<Boolean>
    suspend fun getCurrentUser(): FirebaseCallResult<FirebaseUser>
}
