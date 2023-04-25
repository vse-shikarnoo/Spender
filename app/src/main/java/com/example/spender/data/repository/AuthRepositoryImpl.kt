package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteAuthDaoImpl
import com.example.spender.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDaoImpl: RemoteAuthDaoImpl
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): DataResult<String> {
        return remoteAuthDaoImpl.signIn(email, password)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String
    ): DataResult<FirebaseUser> {
        return remoteAuthDaoImpl.signUp(email, password, nickname)
    }

    override suspend fun signOut(): DataResult<String> {
        return remoteAuthDaoImpl.signOut()
    }

    override suspend fun verifyEmail(): DataResult<String> {
        return remoteAuthDaoImpl.verifyEmail()
    }

    override suspend fun isEmailVerified(): DataResult<Boolean> {
        return remoteAuthDaoImpl.isEmailVerified()
    }

    override suspend fun getCurrentUser(): DataResult<FirebaseUser> {
        return remoteAuthDaoImpl.getCurrentUser()
    }

    override suspend fun checkNickname(nickname: String): DataResult<Boolean> {
        return remoteAuthDaoImpl.checkNickname(nickname)
    }
}
