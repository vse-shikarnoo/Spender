package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.*
import com.example.spender.data.firebase.interfaces.AuthManagerInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.messages.exceptions.FirebaseUndefinedException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthManagerRepository : AuthManagerInterface {
    override suspend fun signIn(email: String, password: String): FirebaseCallResult<String> {
        return try {
            FirebaseInstanceHolder.auth.signInWithEmailAndPassword(email, password).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SIGN_IN_SUCCESS)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun signUp(email: String, password: String): FirebaseCallResult<FirebaseUser> {
        return try {
            val user =
                FirebaseInstanceHolder.auth.createUserWithEmailAndPassword(email, password).await()
            if (user != null)
                FirebaseCallResult.Success(user.user!!)
            else
                FirebaseErrorHandler.handle(FirebaseUndefinedException())
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun isEmailVerified(): FirebaseCallResult<Boolean> {
        return try {
            val bool = FirebaseInstanceHolder.auth.currentUser!!.isEmailVerified
            FirebaseCallResult.Success(bool)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getCurrentUser(): FirebaseCallResult<FirebaseUser> {
        return try {
            val user = FirebaseInstanceHolder.auth.currentUser
            if (user != null) {
                FirebaseInstanceHolder.auth.updateCurrentUser(user)
                FirebaseCallResult.Success(user)
            } else {
                FirebaseErrorHandler.handle(FirebaseUndefinedException())
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun verifyEmail(): FirebaseCallResult<String> {
        return try {
            FirebaseInstanceHolder.auth.currentUser!!.sendEmailVerification().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.VERIFICATION_EMAIL_SENT)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun signOut(): FirebaseCallResult<String> {
        return try {
            FirebaseInstanceHolder.auth.signOut()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SIGN_OUT_SUCCESS)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
