package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.HandleError
import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.interfaces.AuthManagerInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthManagerRepository() : AuthManagerInterface {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            Result.Success(true)
        } catch (e: Exception) {
            HandleError.handleFirebaseError(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (user != null)
                Result.Success(user.user!!)
            else
                Result.Error("sdf")
        } catch (e: Exception) {
            HandleError.handleFirebaseError(e)
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
            HandleError.handleFirebaseError(e)
        }
    }

    override suspend fun getCurrentUser(): Result<FirebaseUser> {
        firebaseAuth.signOut()
        firebaseAuth.currentUser?.let { user ->
            firebaseAuth.updateCurrentUser(user)
            return Result.Success(user)
        }
        return Result.Error("No user signed in")
    }

    override suspend fun verifyEmail(): Result<Boolean> {
        return try {
            firebaseAuth.currentUser!!.sendEmailVerification().await()
            Result.Success(true)
        } catch (e: Exception) {
            HandleError.handleFirebaseError(e)
        }
    }

    override suspend fun resetPassword(): Result<Boolean> {
        return try {
            firebaseAuth.sendPasswordResetEmail(firebaseAuth.currentUser!!.email!!).await()
            Result.Success(true)
        } catch (e: Exception) {
            HandleError.handleFirebaseError(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            Result.Success(true)
        } catch (e: Exception) {
            HandleError.handleFirebaseError(e)
        }
    }
}