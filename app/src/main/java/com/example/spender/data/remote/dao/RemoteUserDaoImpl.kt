package com.example.spender.data.remote.dao

import android.app.Application
import android.util.Log
import com.example.spender.R
import com.example.spender.data.DataResult
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.messages.exceptions.FirebaseUndefinedException
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.dao.UserDao
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteUserDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
): UserDao {
    override suspend fun getUser(
        userId: String?
    ): DataResult<User> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()

            val userDocumentSnapshot = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID).get().await()

            val name = getUserName(userID)
            if (name is DataResult.Error) {
                return name
            }
            val age = getUserAge(userID)
            if (age is DataResult.Error) {
                return age
            }
            val nickname = getUserNickname(userID)
            if (nickname is DataResult.Error) {
                return nickname
            }
            val incomingFriends = getUserIncomingFriends(userID)
            if (incomingFriends is DataResult.Error) {
                return incomingFriends
            }
            val outgoingFriends = getUserOutgoingFriends(userID)
            if (outgoingFriends is DataResult.Error) {
                return outgoingFriends
            }
            val friends = getUserFriends(userID)
            if (friends is DataResult.Error) {
                return friends
            }
            val adminTrips = getUserAdminTrips(userID)
            if (adminTrips is DataResult.Error) {
                return adminTrips
            }
            val passengerTrips = getUserPassengerTrips(userID)
            if (passengerTrips is DataResult.Error) {
                return passengerTrips
            }
            val docRef = userDocumentSnapshot.reference

            DataResult.Success(
                User(
                    name = (name as DataResult.Success).data,
                    age = (age as DataResult.Success).data,
                    nickname = (nickname as DataResult.Success).data,
                    incomingFriends = (incomingFriends as DataResult.Success).data,
                    outgoingFriends = (outgoingFriends as DataResult.Success).data,
                    friends = (friends as DataResult.Success).data,
                    adminTrips = (adminTrips as DataResult.Success).data,
                    passengerTrips = (passengerTrips as DataResult.Success).data,
                    docRef = docRef
                )
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserName(
        userId: String?
    ): DataResult<UserName> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val firstName = userDocRef.get()
                .await().data!![appContext.getString(R.string.collection_users_document_field_first_name)] as String
            val middleName = userDocRef.get()
                .await().data!![appContext.getString(R.string.collection_users_document_field_middle_name)] as String
            val lastName = userDocRef.get()
                .await().data!![appContext.getString(R.string.collection_users_document_field_last_name)] as String
            DataResult.Success(UserName(firstName, middleName, lastName))
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAge(
        userId: String?
    ): DataResult<Long> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val age = userDocRef.get().await().data!![appContext.getString(R.string.collection_users_document_field_age)] as Long
            DataResult.Success(age)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserNickname(
        userId: String?
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val nickname =
                userDocRef.get().await()
                    .data!![appContext.getString(R.string.collection_users_document_field_nickname)] as String
            DataResult.Success(nickname)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserIncomingFriends(
        userId: String?
    ): DataResult<List<Friend>> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val friends = userDocRef.get().await()
                .data!![appContext.getString(R.string.collection_users_document_field_incoming_friends)] as ArrayList<DocumentReference>?

            val lst = mutableListOf<Friend>()

            if (friends != null) {

                for (friend in friends) {
                    val resultName = getUserName(friend.id)
                    val resultNickname = getUserNickname(friend.id)
                    if (resultName is DataResult.Success &&
                        resultNickname is DataResult.Success
                    ) {
                        lst.add(
                            Friend(
                                name = resultName.data,
                                nickname = resultNickname.data,
                                docRef = friend
                            )
                        )
                    } else {
                        Log.d("ABOBA", "GetIncoming undefined")
                        return DataErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserOutgoingFriends(
        userId: String?
    ): DataResult<List<Friend>> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val friends = userDocRef.get().await()
                .data!![appContext.getString(R.string.collection_users_document_field_outgoing_friends)] as ArrayList<DocumentReference>?

            val lst = mutableListOf<Friend>()

            if (friends != null) {

                for (friend in friends) {
                    val resultName = getUserName(friend.id)
                    val resultNickname = getUserNickname(friend.id)
                    if (resultName is DataResult.Success &&
                        resultNickname is DataResult.Success
                    ) {
                        lst.add(
                            Friend(
                                name = resultName.data,
                                nickname = resultNickname.data,
                                docRef = friend
                            )
                        )
                    } else {
                        Log.d("ABOBA", "GetOutgoing undefined")
                        return DataErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserFriends(
        userId: String?
    ): DataResult<List<Friend>> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val friends = userDocRef.get().await()
                .data!![appContext.getString(R.string.collection_users_document_field_friends)] as ArrayList<DocumentReference>?

            val lst = mutableListOf<Friend>()

            if (friends != null) {

                for (friend in friends) {
                    val resultName = getUserName(friend.id)
                    val resultNickname = getUserNickname(friend.id)
                    if (resultName is DataResult.Success &&
                        resultNickname is DataResult.Success
                    ) {
                        lst.add(
                            Friend(
                                name = resultName.data,
                                nickname = resultNickname.data,
                                docRef = friend
                            )
                        )
                    } else {
                        Log.d("ABOBA", "GetUserFriends undefined")
                        return DataErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAdminTrips(
        userId: String?
    ): DataResult<List<Trip>> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val tripCollection: CollectionReference =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_trips))
            val tripsQuery =
                tripCollection.whereEqualTo(appContext.getString(R.string.collection_trip_document_field_creator), userID).get()
                    .await()
            if (!tripsQuery.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuery.documents.forEach { tripDocSnapshot ->
                    lst.add(tripDocSnapshot.toObject(Trip::class.java)!!)
                }
                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserPassengerTrips(
        userId: String?
    ): DataResult<List<Trip>> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val tripCollection: CollectionReference =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_trips))
            val tripsQuery =
                tripCollection.whereNotEqualTo(appContext.getString(R.string.collection_trip_document_field_creator), userID)
            val tripsQuerySnapshot =
                tripsQuery.whereArrayContains(appContext.getString(R.string.collection_trip_document_field_members), userID)
                    .get().await()
            if (!tripsQuerySnapshot.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuerySnapshot.documents.forEach { tripDocSnapshot ->
                    lst.add(tripDocSnapshot.toObject(Trip::class.java)!!)
                }
                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUser(
        userId: String?,
        newUser: User
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID).set(newUser).await()
            DataResult.Success(FirebaseSuccessMessages.USER_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserName(
        userId: String?,
        newName: UserName
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_first_name), newName.firstName)
                .await()
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_middle_name), newName.middleName)
                .await()
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_last_name), newName.lastName).await()
            DataResult.Success(FirebaseSuccessMessages.USER_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserAge(
        userId: String?,
        newAge: Int,
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_age), newAge).await()
            DataResult.Success(FirebaseSuccessMessages.USER_AGE_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserNickname(
        userId: String?,
        newNickname: String,
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            when (val checkNicknameResult = checkNickname(newNickname)) {
                is DataResult.Success -> {
                    if (checkNicknameResult.data) {
                        val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
                        userDocRef.update(appContext.getString(R.string.collection_users_document_field_nickname), newNickname)
                            .await()
                        DataResult.Success(FirebaseSuccessMessages.USER_NICKNAME_UPDATED)
                    } else {
                        DataErrorHandler.handle(FirebaseNicknameException())
                    }
                }
                is DataResult.Error -> {
                    checkNicknameResult
                }
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun addUserIncomingFriend(
        userId: String?,
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
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


    override suspend fun addUserOutgoingFriend(
        userId: String?,
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayUnion(friend.docRef)
            )

            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayUnion(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserFriend(
        userId: String?,
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
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
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
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
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
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
            val checkNicknameQuerySnapshot =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .whereEqualTo(appContext.getString(R.string.collection_users_document_field_nickname), nickname)
                    .get().await()
            DataResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
