package com.example.spender.data.firebase.repositories

import android.util.Log
import com.example.spender.data.firebase.*
import com.example.spender.data.firebase.databaseFieldNames.CollectionNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.interfaces.AuthRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.firebase.messages.exceptions.FirebaseNoUserSignedInException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

class AuthRepository : AuthRepositoryInterface {
    override suspend fun signIn(email: String, password: String): FirebaseCallResult<String> {
        return try {
            FirebaseInstanceHolder.auth.signInWithEmailAndPassword(email, password).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SIGN_IN_SUCCESS)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String
    ): FirebaseCallResult<FirebaseUser> {
        return when (val checkNickname = checkNickname(nickname)) {
            is FirebaseCallResult.Success -> {
                if (checkNickname.data) {
                    val user = try {
                        FirebaseInstanceHolder.auth.createUserWithEmailAndPassword(
                            email,
                            password
                        ).await()
                    } catch (e: Exception) {
                        Log.d("ABOBA", "error auth - ${e.message ?: "error auth"}")
                        return FirebaseErrorHandler.handle(e)
                    }
                    return try {
                        user.user!!.let {
                            FirebaseInstanceHolder.db.collection(CollectionNames.USER)
                                .document(it.uid)
                                .set(
                                    mapOf(
                                        CollectionUserDocumentFieldNames.FIRST_NAME to "",
                                        CollectionUserDocumentFieldNames.MIDDLE_NAME to "",
                                        CollectionUserDocumentFieldNames.LAST_NAME to "",
                                        CollectionUserDocumentFieldNames.AGE to 0,
                                        CollectionUserDocumentFieldNames.NICKNAME to nickname,
                                        CollectionUserDocumentFieldNames.INCOMING_FRIENDS to emptyList<DocumentReference>(),
                                        CollectionUserDocumentFieldNames.OUTGOING_FRIENDS to emptyList<DocumentReference>(),
                                        CollectionUserDocumentFieldNames.FRIENDS to emptyList<DocumentReference>(),
                                        CollectionUserDocumentFieldNames.TRIPS to emptyList<DocumentReference>(),
                                    )
                                ).await()
                            FirebaseCallResult.Success(it)
                        }
                    } catch (e: Exception) {
                        user.user?.delete()?.await()
                        Log.d("ABOBA", "Firestore error")
                        FirebaseErrorHandler.handle(e)
                    }
                } else {
                    Log.d("ABOBA", "check nickname = false ${FirebaseNicknameException().message}")
                    FirebaseErrorHandler.handle(FirebaseNicknameException())
                }
            }
            is FirebaseCallResult.Error -> {
                Log.d("ABOBA", "check nickname - ${checkNickname.exception}")
                checkNickname
            }
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
                FirebaseErrorHandler.handle(FirebaseNoUserSignedInException())
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

    override suspend fun checkNickname(
        nickname: String
    ): FirebaseCallResult<Boolean> {
        if (nickname.length < 6) {
            return FirebaseErrorHandler.handle(FirebaseNicknameLengthException())
        }
        return try {
            val checkNicknameQuerySnapshot =
                FirebaseInstanceHolder.db.collection(CollectionNames.USER)
                    .whereEqualTo(CollectionUserDocumentFieldNames.NICKNAME, nickname)
                    .get().await()
            FirebaseCallResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
