package com.example.spender.data.remote.dao

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.messages.exceptions.FirebaseAddMyselfFriendException
import com.example.spender.data.messages.exceptions.FirebaseAlreadyFriendException
import com.example.spender.data.messages.exceptions.FirebaseAlreadySentFriendException
import com.example.spender.data.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.messages.exceptions.FirebaseNoNicknameUserException
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName
import com.example.spender.domain.remotedao.RemoteUserDao
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteUserDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteUserDao {
    private val sharedFunctions = SharedFunctions(remoteDataSource, appContext)
    override var source: Source = Source.SERVER

    /*
     * getUserName
     */

    override suspend fun getUserName(): DataResult<UserName> {
        return sharedFunctions.getUserName(null, source)
    }

    /*
     * getUserAge
     */

    override suspend fun getUserAge(): DataResult<Long> {
        return try {
            val age = sharedFunctions.getUserDocRef(null).get(source).await().data!![
                    appContext.getString(
                        R.string.collection_users_document_field_age
                    )] as Long
            DataResult.Success(age)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserNickname
     */

    override suspend fun getUserNickname(): DataResult<String> {
        return sharedFunctions.getUserNickname(null, source)
    }

    /*
     * getUserIncomingFriends
     */

    override suspend fun getUserIncomingFriends(): DataResult<List<Friend>> {
        return try {
            val friendsDocRefs = sharedFunctions.getUserDocRef(null).get(source).await()
                .data!![appContext.getString(
                R.string.collection_users_document_field_incoming_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = sharedFunctions.assembleFriendsList(friendsDocRefs, source)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserOutgoingFriends
     */

    override suspend fun getUserOutgoingFriends(): DataResult<List<Friend>> {
        return try {
            val friendsDocRefs = sharedFunctions.getUserDocRef(null).get(source).await()
                .data!![appContext.getString(
                R.string.collection_users_document_field_outgoing_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = sharedFunctions.assembleFriendsList(friendsDocRefs, source)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserFriends
     */

    override suspend fun getUserFriends(): DataResult<List<Friend>> {
        return try {
            val friendsDocRefs = sharedFunctions.getUserDocRef(null).get(source).await().data!![
                    appContext.getString(
                        R.string.collection_users_document_field_friends
                    )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = sharedFunctions.assembleFriendsList(friendsDocRefs, source)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * updateUserName
     */

    override suspend fun updateUserName(
        newName: UserName
    ): DataResult<String> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_first_name),
                newName.firstName
            ).await()
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_middle_name),
                newName.middleName
            ).await()
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_last_name),
                newName.lastName
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * updateUserAge
     */

    override suspend fun updateUserAge(
        newAge: Int,
    ): DataResult<String> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_age),
                newAge
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_AGE_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * updateUserNickname
     */

    override suspend fun updateUserNickname(
        newNickname: String,
    ): DataResult<String> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)

            val checkNicknameResult = checkNickname(newNickname)
            if (checkNicknameResult is DataResult.Error) {
                return checkNicknameResult
            }

            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_nickname),
                newNickname
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_NICKNAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * addUserIncomingFriend
     */

    override suspend fun addUserIncomingFriend(
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayUnion(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayUnion(userDocRef)
            )

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * addUserOutgoingFriend
     */

    override suspend fun addUserOutgoingFriend(
        nickname: String
    ): DataResult<String> {

        // nickname Exists
        val nicknameExists = checkExistNickname(nickname)
        if (nicknameExists is DataResult.Error) {
            return nicknameExists
        }

        val userDocRef = sharedFunctions.getUserDocRef(null)
        val friendDocRef = (nicknameExists as DataResult.Success).data

        // already friend
        val alreadyFriend = checkAlreadyFriend(friendDocRef)
        if (alreadyFriend is DataResult.Error) {
            return alreadyFriend
        }

        // me
        if (userDocRef == friendDocRef) {
            return DataErrorHandler.handle(FirebaseAddMyselfFriendException())
        }

        // already incoming
        val alreadyIncoming = checkAlreadyIncoming(friendDocRef)
        if (alreadyIncoming is DataResult.Error) {
            return alreadyIncoming
        }
        if ((alreadyIncoming as DataResult.Success).data != null) {
            return addUserIncomingFriend((alreadyIncoming).data!!)
        }

        // already outgoing
        val alreadyOutgoing = checkAlreadyOutgoing(friendDocRef)
        if (alreadyOutgoing is DataResult.Error) {
            return alreadyOutgoing
        }

        return try {
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayUnion(friendDocRef)
            )

            batch.update(
                friendDocRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayUnion(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun checkExistNickname(
        nickname: String
    ): DataResult<DocumentReference> {
        return try {
            val querySnapshot = remoteDataSource.db.collection(
                appContext.getString(R.string.collection_name_users)
            ).whereEqualTo(
                appContext.getString(R.string.collection_users_document_field_nickname),
                nickname
            ).get().await()
            if (querySnapshot.documents.isEmpty()) {
                return DataErrorHandler.handle(FirebaseNoNicknameUserException())
            }
            DataResult.Success(querySnapshot.documents.first().reference)
        } catch (e: Exception) {
            return DataErrorHandler.handle(e)
        }
    }

    override suspend fun checkAlreadyFriend(
        friendDocRef: DocumentReference
    ): DataResult<Boolean> {
        val friendsResult = getUserFriends()
        if (friendsResult is DataResult.Error) {
            return friendsResult
        }

        if ((friendsResult as DataResult.Success).data.find {
                it.docRef == friendDocRef
            } != null
        ) {
            return DataErrorHandler.handle(FirebaseAlreadyFriendException())
        }
        return DataResult.Success(false)
    }

    override suspend fun checkAlreadyIncoming(
        friendDocRef: DocumentReference
    ): DataResult<Friend?> {
        val incomingFriendsResult = getUserIncomingFriends()
        if (incomingFriendsResult is DataResult.Error) {
            return incomingFriendsResult
        }

        (incomingFriendsResult as DataResult.Success).data.forEach {
            if (it.docRef.path == friendDocRef.path) {
                return DataResult.Success(it)
            }
        }
        return DataResult.Success(null)
    }

    override suspend fun checkAlreadyOutgoing(
        friendDocRef: DocumentReference
    ): DataResult<Boolean> {
        val outgoingFriendsResult = getUserOutgoingFriends()
        if (outgoingFriendsResult is DataResult.Error) {
            return outgoingFriendsResult
        }
        (outgoingFriendsResult as DataResult.Success).data.forEach {
            if (it.docRef == friendDocRef) {
                return DataErrorHandler.handle(FirebaseAlreadySentFriendException())
            }
        }
        return DataResult.Success(true)
    }

    /*
     * removeUserFriend
     */

    override suspend fun removeUserFriend(
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_FRIEND_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserOutgoingFriend(
        friend: Friend
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserIncomingFriend(
        friend: Friend
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_REMOVED)
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
            val checkNicknameQuerySnapshot = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).whereEqualTo(
                appContext.getString(R.string.collection_users_document_field_nickname),
                nickname
            ).get(source).await()
            if (!checkNicknameQuerySnapshot.isEmpty) {
                DataErrorHandler.handle(FirebaseNicknameException())
            }
            DataResult.Success(true)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
