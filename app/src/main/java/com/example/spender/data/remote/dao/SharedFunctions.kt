package com.example.spender.data.remote.dao

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class SharedFunctions(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application,
) {
    fun getUserDocRef(userId: String?): DocumentReference {
        val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
        return remoteDataSource.db.collection(
            appContext.getString(
                R.string.collection_name_users
            )
        ).document(userID)
    }

    suspend fun getUserName(userId: String?, source: Source): DataResult<UserName> {
        return try {
            val userName = assembleUserName(userId, source)
            if (userName is DataResult.Error) {
                return userName
            }
            DataResult.Success((userName as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleUserName(userId: String?, source: Source): DataResult<UserName> {
        val userDocRef = getUserDocRef(userId)
        val firstName = userDocRef.get(source).await().data!![appContext.getString(
            R.string.collection_users_document_field_first_name
        )] as String
        val middleName = userDocRef.get(source).await().data!![appContext.getString(
            R.string.collection_users_document_field_middle_name
        )] as String
        val lastName = userDocRef.get(source).await().data!![appContext.getString(
            R.string.collection_users_document_field_last_name
        )] as String
        return DataResult.Success(UserName(firstName, middleName, lastName))
    }

    suspend fun getUserNickname(userId: String?, source: Source): DataResult<String> {
        return try {
            val nickname = getUserDocRef(userId).get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_nickname
            )] as String
            DataResult.Success(nickname)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    suspend fun assembleFriendsList(
        friendsDocRefs: ArrayList<DocumentReference>,
        source: Source
    ): DataResult<List<Friend>> {
        return DataResult.Success(
            buildList {
                friendsDocRefs.forEach { friendDocRef ->
                    val friend = assembleFriend(friendDocRef, source)
                    if (friend is DataResult.Error) {
                        return friend
                    }
                    this.add((friend as DataResult.Success).data)
                }
            }
        )
    }

    suspend fun assembleFriend(
        friendDocRef: DocumentReference,
        source: Source
    ): DataResult<Friend> {
        val resultName = getUserName(friendDocRef.id, source)
        if (resultName is DataResult.Error) {
            return resultName
        }
        val resultNickname = getUserNickname(friendDocRef.id, source)
        if (resultNickname is DataResult.Error) {
            return resultNickname
        }
        return DataResult.Success(
            Friend(
                name = (resultName as DataResult.Success).data,
                nickname = (resultNickname as DataResult.Success).data,
                docRef = friendDocRef
            )
        )
    }
}