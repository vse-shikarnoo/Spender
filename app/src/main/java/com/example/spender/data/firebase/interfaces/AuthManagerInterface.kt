package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.Result
import com.google.firebase.auth.FirebaseUser

interface AuthManagerInterface {
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun signOut(): Result<Boolean>
    suspend fun verifyEmail(): Result<Boolean>
    suspend fun isEmailVerified(): Result<Boolean>
    suspend fun getCurrentUser(): Result<FirebaseUser>
    suspend fun resetPassword(): Result<Boolean>
}
