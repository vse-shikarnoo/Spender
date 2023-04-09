package com.example.spender.data.firebase.repositories

import android.app.Application
import android.util.Log
import com.example.spender.R
import com.example.spender.data.firebase.*
import com.example.spender.data.firebase.repositoryInterfaces.AuthRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.firebase.messages.exceptions.FirebaseNoUserSignedInException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val appContext: Application
) : AuthRepositoryInterface {
    override suspend fun signIn(email: String, password: String): FirebaseCallResult<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
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
                        auth.createUserWithEmailAndPassword(
                            email,
                            password
                        ).await()
                    } catch (e: Exception) {
                        Log.d("ABOBA", "error auth - ${e.message ?: "error auth"}")
                        return FirebaseErrorHandler.handle(e)
                    }
                    return try {
                        user.user!!.let {
                            db.collection(appContext.getString(R.string.collection_name_users))
                                .document(it.uid)
                                .set(
                                    mapOf(
                                        appContext.getString(R.string.collection_users_document_field_first_name) to "",
                                        appContext.getString(R.string.collection_users_document_field_middle_name) to "",
                                        appContext.getString(R.string.collection_users_document_field_last_name) to "",
                                        appContext.getString(R.string.collection_users_document_field_age) to 0,
                                        appContext.getString(R.string.collection_users_document_field_nickname) to nickname,
                                        appContext.getString(R.string.collection_users_document_field_incoming_friends) to emptyList<DocumentReference>(),
                                        appContext.getString(R.string.collection_users_document_field_outgoing_friends) to emptyList<DocumentReference>(),
                                        appContext.getString(R.string.collection_users_document_field_friends) to emptyList<DocumentReference>(),
                                        appContext.getString(R.string.collection_users_document_field_trips) to emptyList<DocumentReference>(),
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
            val bool = auth.currentUser!!.isEmailVerified
            FirebaseCallResult.Success(bool)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getCurrentUser(): FirebaseCallResult<FirebaseUser> {
        return try {
            val user = auth.currentUser
            if (user != null) {
                auth.updateCurrentUser(user)
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
            auth.currentUser!!.sendEmailVerification().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.VERIFICATION_EMAIL_SENT)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun signOut(): FirebaseCallResult<String> {
        return try {
            auth.signOut()
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
                db.collection(appContext.getString(R.string.collection_name_users))
                    .whereEqualTo(
                        appContext.getString(R.string.collection_users_document_field_nickname),
                        nickname
                    )
                    .get().await()
            FirebaseCallResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
