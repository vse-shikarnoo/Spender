package com.example.spender.data.remote.dao

import android.app.Application
import android.util.Log
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.messages.exceptions.FirebaseNoUserSignedInException
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.remotedao.RemoteAuthDao
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class RemoteAuthDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteAuthDao {
    override suspend fun signIn(email: String, password: String): DataResult<String> {
        return try {
            remoteDataSource.auth.signInWithEmailAndPassword(email, password).await()
            DataResult.Success(FirebaseSuccessMessages.SIGN_IN_SUCCESS)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String
    ): DataResult<FirebaseUser> {
        return when (val checkNickname = checkNickname(nickname)) {
            is DataResult.Success -> {
                if (checkNickname.data) {
                    val user = try {
                        remoteDataSource.auth.createUserWithEmailAndPassword(
                            email,
                            password
                        ).await()
                    } catch (e: Exception) {
                        Log.d("ABOBA", "error auth - ${e.message ?: "error auth"}")
                        return DataErrorHandler.handle(e)
                    }
                    return try {
                        user.user!!.let {
                            remoteDataSource.db.collection(
                                appContext.getString(R.string.collection_name_users)
                            )
                                .document(it.uid)
                                .set(signUpMap(nickname)).await()
                            DataResult.Success(it)
                        }
                    } catch (e: Exception) {
                        user.user?.delete()?.await()
                        Log.d("ABOBA", "Firestore error")
                        DataErrorHandler.handle(e)
                    }
                } else {
                    Log.d("ABOBA", "check nickname = false ${FirebaseNicknameException().message}")
                    DataErrorHandler.handle(FirebaseNicknameException())
                }
            }

            is DataResult.Error -> {
                Log.d("ABOBA", "check nickname - ${checkNickname.exception}")
                checkNickname
            }
        }
    }

    private fun signUpMap(nickname: String): Map<String, Any> {
        return mapOf(
            appContext.getString(
                R.string.collection_users_document_field_first_name
            ) to "",
            appContext.getString(
                R.string.collection_users_document_field_middle_name
            ) to "",
            appContext.getString(
                R.string.collection_users_document_field_last_name
            ) to "",
            appContext.getString(
                R.string.collection_users_document_field_age
            ) to 0,
            appContext.getString(
                R.string.collection_users_document_field_nickname
            ) to nickname,
            appContext.getString(
                R.string.collection_users_document_field_incoming_friends
            ) to emptyList<DocumentReference>(),
            appContext.getString(
                R.string.collection_users_document_field_outgoing_friends
            ) to emptyList<DocumentReference>(),
            appContext.getString(
                R.string.collection_users_document_field_friends
            ) to emptyList<DocumentReference>(),
            appContext.getString(
                R.string.collection_users_document_field_trips
            ) to emptyList<DocumentReference>(),
        )
    }

    override suspend fun isEmailVerified(): DataResult<Boolean> {
        return try {
            val bool = remoteDataSource.auth.currentUser!!.isEmailVerified
            DataResult.Success(bool)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getCurrentUser(): DataResult<FirebaseUser> {
        return try {
            val user = remoteDataSource.auth.currentUser
            if (user != null) {
                remoteDataSource.auth.updateCurrentUser(user)
                DataResult.Success(user)
            } else {
                DataErrorHandler.handle(FirebaseNoUserSignedInException())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun verifyEmail(): DataResult<String> {
        return try {
            remoteDataSource.auth.currentUser!!.sendEmailVerification().await()
            DataResult.Success(FirebaseSuccessMessages.VERIFICATION_EMAIL_SENT)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun signOut(): DataResult<String> {
        return try {
            remoteDataSource.auth.signOut()
            DataResult.Success(FirebaseSuccessMessages.SIGN_OUT_SUCCESS)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun checkNickname(
        nickname: String
    ): DataResult<Boolean> {
        if (nickname.length < 6) {
            return DataErrorHandler.handle(FirebaseNicknameLengthException())
        }
        return try {
            val checkNicknameQuerySnapshot =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .whereEqualTo(
                        appContext.getString(R.string.collection_users_document_field_nickname),
                        nickname
                    )
                    .get().await()
            DataResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
