package com.example.spender.data.firebase.repositoryInterfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepositoryInterface {
    suspend fun signIn(email: String, password: String): FirebaseCallResult<String>
    suspend fun signUp(email: String, password: String, nickname: String): FirebaseCallResult<FirebaseUser>
    suspend fun signOut(): FirebaseCallResult<String>
    suspend fun verifyEmail(): FirebaseCallResult<String>
    suspend fun isEmailVerified(): FirebaseCallResult<Boolean>
    suspend fun getCurrentUser(): FirebaseCallResult<FirebaseUser>
    suspend fun checkNickname(nickname: String): FirebaseCallResult<Boolean>
}
