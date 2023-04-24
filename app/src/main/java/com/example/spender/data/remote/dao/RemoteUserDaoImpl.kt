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
import com.example.spender.domain.remotedao.RemoteUserDao
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteUserDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteUserDao {
    override var source: Source = Source.SERVER

    override suspend fun getUser(): DataResult<User> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()

            val userDocumentSnapshot =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID).get(source).await()

            val name = getUserName(userID)
            if (name is DataResult.Error) {
                return name
            }
            val age = getUserAge()
            if (age is DataResult.Error) {
                return age
            }
            val nickname = getUserNickname(userID)
            if (nickname is DataResult.Error) {
                return nickname
            }
            val incomingFriends = getUserIncomingFriends()
            if (incomingFriends is DataResult.Error) {
                return incomingFriends
            }
            val outgoingFriends = getUserOutgoingFriends()
            if (outgoingFriends is DataResult.Error) {
                return outgoingFriends
            }
            val friends = getUserFriends()
            if (friends is DataResult.Error) {
                return friends
            }
            val adminTrips = getUserAdminTrips()
            if (adminTrips is DataResult.Error) {
                return adminTrips
            }
            val passengerTrips = getUserPassengerTrips()
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

    override suspend fun getUserName(userId: String?): DataResult<UserName> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            val firstName = userDocRef.get(source)
                .await().data!![appContext.getString(R.string.collection_users_document_field_first_name)] as String
            val middleName = userDocRef.get(source)
                .await().data!![appContext.getString(R.string.collection_users_document_field_middle_name)] as String
            val lastName = userDocRef.get(source)
                .await().data!![appContext.getString(R.string.collection_users_document_field_last_name)] as String
            DataResult.Success(UserName(firstName, middleName, lastName))
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAge(): DataResult<Long> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            val age = userDocRef.get(source)
                .await().data!![appContext.getString(R.string.collection_users_document_field_age)] as Long
            DataResult.Success(age)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserNickname(userId: String?): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            val nickname =
                userDocRef.get(source).await()
                    .data!![appContext.getString(R.string.collection_users_document_field_nickname)] as String
            DataResult.Success(nickname)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserIncomingFriends(): DataResult<List<Friend>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            val friends = userDocRef.get(source).await()
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

    override suspend fun getUserOutgoingFriends(): DataResult<List<Friend>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            val friends = userDocRef.get(source).await()
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

    override suspend fun getUserFriends(): DataResult<List<Friend>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            val friends = userDocRef.get(source).await()
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

    override suspend fun getUserTrips(): DataResult<List<Trip>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val tripCollection: CollectionReference =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_trips))
            val tripsQuerySnapshot =
                tripCollection.whereArrayContains(
                    appContext.getString(R.string.collection_trip_document_field_members),
                    userDocRef
                ).get(source).await()
            if (!tripsQuerySnapshot.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuerySnapshot.documents.forEach { tripDocSnapshot ->
                    val trip = getTrip(tripDocSnapshot.reference)
                    if (trip is DataResult.Error) {
                        return trip
                    }
                    lst.add((trip as DataResult.Success).data)
                }
                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAdminTrips(): DataResult<List<Trip>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val tripCollection: CollectionReference =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_trips))
            val tripsQuery =
                tripCollection.whereEqualTo(
                    appContext.getString(R.string.collection_trip_document_field_creator),
                    userID
                ).get(source)
                    .await()
            if (!tripsQuery.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuery.documents.forEach { tripDocSnapshot ->
                    val trip = getTrip(tripDocSnapshot.reference)
                    if (trip is DataResult.Error) {
                        return trip
                    }
                    lst.add((trip as DataResult.Success).data)
                }
                DataResult.Success(lst)
            } else {
                DataResult.Success(emptyList())
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getUserPassengerTrips(): DataResult<List<Trip>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val tripCollection: CollectionReference =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_trips))
            val tripsQuery =
                tripCollection.whereNotEqualTo(
                    appContext.getString(R.string.collection_trip_document_field_creator),
                    userID
                )
            val tripsQuerySnapshot =
                tripsQuery.whereArrayContains(
                    appContext.getString(R.string.collection_trip_document_field_members),
                    userID
                )
                    .get(source).await()
            if (!tripsQuerySnapshot.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuerySnapshot.documents.forEach { tripDocSnapshot ->
                    val trip = getTrip(tripDocSnapshot.reference)
                    if (trip is DataResult.Error) {
                        return trip
                    }
                    lst.add((trip as DataResult.Success).data)
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
        newUser: User
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                .document(userID).set(newUser).await()
            DataResult.Success(FirebaseSuccessMessages.USER_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserName(
        newName: UserName
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_first_name),
                newName.firstName
            )
                .await()
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_middle_name),
                newName.middleName
            )
                .await()
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_last_name),
                newName.lastName
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserAge(
        newAge: Int,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_age),
                newAge
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_AGE_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserNickname(
        newNickname: String,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            when (val checkNicknameResult = checkNickname(newNickname)) {
                is DataResult.Success -> {
                    if (checkNicknameResult.data) {
                        val userDocRef =
                            remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                                .document(userID)
                        userDocRef.update(
                            appContext.getString(R.string.collection_users_document_field_nickname),
                            newNickname
                        )
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
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
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
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
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
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
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
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
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
            val userDocRef =
                remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                    .document(userID)
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
                    .whereEqualTo(
                        appContext.getString(R.string.collection_users_document_field_nickname),
                        nickname
                    )
                    .get(source).await()
            DataResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getTrip(tripDocRef: DocumentReference): DataResult<Trip> {
        return try {
            val tripDocSnapshot = tripDocRef.get(source).await()

            val tripCreatorDocRef = tripDocSnapshot.get(
                appContext.getString(R.string.collection_trip_document_field_creator)
            ) as DocumentReference
            val tripCreatorName = getUserName(tripCreatorDocRef.id)
            val tripCreatorNickname = getUserNickname(tripCreatorDocRef.id)
            if (tripCreatorName is DataResult.Error) {
                return tripCreatorName
            }
            if (tripCreatorNickname is DataResult.Error) {
                return tripCreatorNickname
            }
            val tripCreator = Friend(
                (tripCreatorName as DataResult.Success).data,
                (tripCreatorNickname as DataResult.Success).data,
                tripCreatorDocRef
            )

            val tripName = tripDocSnapshot.get(
                appContext.getString(R.string.collection_trip_document_field_name)
            ) as String

            val tripMembersSnapshot = tripDocSnapshot.get(
                appContext.getString(R.string.collection_trip_document_field_members)
            ) as ArrayList<DocumentReference>?

            val tripMembers = mutableListOf<Friend>()
            if (tripMembersSnapshot != null) {
                for (tripMemberDocRef in tripMembersSnapshot) {
                    val tripMemberName = getUserName(tripMemberDocRef.id)
                    val tripMemberNickname = getUserNickname(tripMemberDocRef.id)
                    if (tripMemberName is DataResult.Error) {
                        return tripMemberName
                    }
                    if (tripMemberNickname is DataResult.Error) {
                        return tripMemberNickname
                    }
                    val tripMember = Friend(
                        (tripMemberName as DataResult.Success).data,
                        (tripMemberNickname as DataResult.Success).data,
                        tripMemberDocRef
                    )
                    tripMembers.add(tripMember)
                }
            }

            DataResult.Success(
                Trip(
                    tripName,
                    tripCreator,
                    tripMembers,
                    emptyList(),
                    tripDocRef
                )
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
