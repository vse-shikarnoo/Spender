package com.example.spender.domain.dao

import com.example.spender.data.DataResult
import com.google.firebase.auth.FirebaseUser

interface AuthDao {
    suspend fun signIn(email: String, password: String): DataResult<String>
    suspend fun signUp(email: String, password: String, nickname: String): DataResult<FirebaseUser>
    suspend fun signOut(): DataResult<String>
    suspend fun verifyEmail(): DataResult<String>
    suspend fun isEmailVerified(): DataResult<Boolean>
    suspend fun getCurrentUser(): DataResult<FirebaseUser>
    suspend fun checkNickname(nickname: String): DataResult<Boolean>
}