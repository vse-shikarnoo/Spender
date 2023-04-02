package com.example.spender.data.firebase.authManager

import AuthManager
import com.example.spender.data.firebase.Result
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager() : AuthManager {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> {
                    Result.Error("Network error")
                }
                is FirebaseAuthInvalidUserException -> {
                    Result.Error("Invalid email or password")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error("Invalid email or password")
                }
                else -> {
                    Result.Error("An error occurred")
                }
            }
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> {
                    Result.Error("Network error")
                }
                is FirebaseAuthInvalidUserException -> {
                    Result.Error("Invalid email or password")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error("Invalid email or password")
                }
                else -> {
                    Result.Error("An error occurred")
                }
            }
        }
    }

    override suspend fun isEmailVerified(): Result<Boolean> {
        return try {
            val bool = firebaseAuth.currentUser!!.isEmailVerified
            if (bool)
                Result.Success(true)
            else
                Result.Error("Email not verified")
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> {
                    Result.Error("Network error")
                }
                is FirebaseAuthInvalidUserException -> {
                    Result.Error("Invalid email or password")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error("Invalid email or password")
                }
                else -> {
                    Result.Error("An error occurred")
                }
            }
        }
    }

    override suspend fun verifyEmail(): Result<Boolean> {
        return try {
            firebaseAuth.currentUser!!.sendEmailVerification().await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> {
                    Result.Error("Network error")
                }
                is FirebaseAuthInvalidUserException -> {
                    Result.Error("Invalid email or password")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error("Invalid email or password")
                }
                else -> {
                    Result.Error("An error occurred")
                }
            }
        }
    }

    override suspend fun resetPassword(): Result<Boolean> {
        return try {
            firebaseAuth.sendPasswordResetEmail(firebaseAuth.currentUser!!.email!!).await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> {
                    Result.Error("Network error")
                }
                is FirebaseAuthInvalidUserException -> {
                    Result.Error("Invalid email or password")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error("Invalid email or password")
                }
                else -> {
                    Result.Error("An error occurred")
                }
            }
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> {
                    Result.Error("Network error")
                }
                is FirebaseAuthInvalidUserException -> {
                    Result.Error("Invalid email or password")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error("Invalid email or password")
                }
                else -> {
                    Result.Error("An error occurred")
                }
            }
        }
    }
}

//suspend fun test() {
//    val authManager = FirebaseAuthManager()
//    val signInResult = authManager.signIn("example@email.com", "password123")
//    if (signInResult is Result.Error) {
//        signInResult.exception
//    }
//}