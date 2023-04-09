package com.example.spender.data.firebase.interfaces

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

interface AuthRepositoryInterface {
    suspend fun signIn(email: String, password: String): FirebaseCallResult<String>
    suspend fun signUp(email: String, password: String, nickname: String): FirebaseCallResult<FirebaseUser>
    suspend fun signOut(): FirebaseCallResult<String>
    suspend fun verifyEmail(): FirebaseCallResult<String>
    suspend fun isEmailVerified(): FirebaseCallResult<Boolean>
    suspend fun getCurrentUser(): FirebaseCallResult<FirebaseUser>
    suspend fun checkNickname(nickname: String): FirebaseCallResult<Boolean>
}
