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
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.example.spender.domain.remotedao.RemoteUserDao
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class RemoteUserDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteUserDao {
    override var source: Source = Source.SERVER

    /*
     * getUser
     */

    override suspend fun getUser(): DataResult<User> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()

            val userDocRef =
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                ).document(userID)

            val user = assembleUser(userID, userDocRef)
            if (user is DataResult.Error) {
                return user
            }

            DataResult.Success((user as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleUser(
        userID: String,
        userDocRef: DocumentReference
    ): DataResult<User> {
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

        return DataResult.Success(
            User(
                name = (name as DataResult.Success).data,
                age = (age as DataResult.Success).data,
                nickname = (nickname as DataResult.Success).data,
                incomingFriends = (incomingFriends as DataResult.Success).data,
                outgoingFriends = (outgoingFriends as DataResult.Success).data,
                friends = (friends as DataResult.Success).data,
                adminTrips = (adminTrips as DataResult.Success).data,
                passengerTrips = (passengerTrips as DataResult.Success).data,
                docRef = userDocRef
            )
        )
    }

    /*
     * getUserName
     */

    override suspend fun getUserName(userId: String?): DataResult<UserName> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)

            val userName = assembleUserName(userDocRef)
            if (userName is DataResult.Error) {
                return userName
            }

            DataResult.Success((userName as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleUserName(userDocRef: DocumentReference): DataResult<UserName> {
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

    /*
     * getUserAge
     */

    override suspend fun getUserAge(): DataResult<Long> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val age = userDocRef.get(source).await().data!![appContext.getString(
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

    override suspend fun getUserNickname(userId: String?): DataResult<String> {
        return try {
            val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val nickname = userDocRef.get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_nickname
            )] as String
            DataResult.Success(nickname)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserIncomingFriends
     */

    override suspend fun getUserIncomingFriends(): DataResult<List<Friend>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)

            val friendsDocRefs = userDocRef.get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_incoming_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = assembleFriendsList(friendsDocRefs)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleFriendsList(
        friendsDocRefs: ArrayList<DocumentReference>
    ): DataResult<List<Friend>> {
        return DataResult.Success(
            buildList {
                friendsDocRefs.forEach { friendDocRef ->
                    val friend = assembleFriend(friendDocRef)
                    if (friend is DataResult.Error) {
                        return friend
                    }
                    this.add((friend as DataResult.Success).data)
                }
            }
        )
    }

    private suspend fun assembleFriend(friendDocRef: DocumentReference): DataResult<Friend> {
        val resultName = getUserName(friendDocRef.id)
        if (resultName is DataResult.Error) {
            return resultName
        }
        val resultNickname = getUserNickname(friendDocRef.id)
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

    /*
     * getUserOutgoingFriends
     */

    override suspend fun getUserOutgoingFriends(): DataResult<List<Friend>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(R.string.collection_name_users)
            ).document(userID)
            val friendsDocRefs = userDocRef.get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_outgoing_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = assembleFriendsList(friendsDocRefs)
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
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val friendsDocRefs = userDocRef.get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = assembleFriendsList(friendsDocRefs)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserTrips
     */

    /**
     * TODO()
     */

    override suspend fun getUserTrips(): DataResult<List<Trip>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val userTripsDocRefs = userDocRef.get(source).await().get(
                appContext.getString(R.string.collection_users_document_field_trips)
            ) as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val tripTasks = buildList {
                userTrips.forEach { tripDocRef ->
                    this.add(
                        withContext(Dispatchers.IO) {
                            async {
                                getTrip(tripDocRef)
                            }
                        }
                    )
                }
            }

            DataResult.Success(
                buildList {
                    tripTasks.awaitAll().forEach { trip ->
                        if (trip is DataResult.Error) {
                            return trip
                        }
                        this.add((trip as DataResult.Success).data)
                    }
                }
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }



    /*
     * getUserAdminTrips
     */

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
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
                            remoteDataSource.db.collection(
                                appContext.getString(R.string.collection_name_users)
                            )
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
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
        nickname: String
    ): DataResult<String> {
        val querySnapshot = remoteDataSource.db.collection(
            appContext.getString(R.string.collection_name_users)
        ).whereEqualTo(
            appContext.getString(R.string.collection_users_document_field_nickname),
            nickname
        ).get().await()
        if (querySnapshot.isEmpty) {
            return DataErrorHandler.handle(FirebaseNoNicknameUserException())
        }
        val friendDocRef = querySnapshot.documents[0].reference

        val friendsResult = getUserFriends()
        if (friendsResult is DataResult.Error) {
            return friendsResult
        }

        val userID = remoteDataSource.auth.currentUser?.uid.toString()
        val userDocRef =
            remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)

        val isAlreadyFriend = (friendsResult as DataResult.Success).data.find {
            it.docRef == friendDocRef
        } != null
        if (isAlreadyFriend) {
            return DataErrorHandler.handle(FirebaseAlreadyFriendException())
        }
        val isMe = userDocRef == friendDocRef
        if (isMe) {
            return DataErrorHandler.handle(FirebaseAddMyselfFriendException())
        }

        val incomingFriendsResult = getUserIncomingFriends()
        if (incomingFriendsResult is DataResult.Error) {
            return incomingFriendsResult
        }

        (incomingFriendsResult as DataResult.Success).data.forEach {
            if (it.docRef == friendDocRef) {
                return addUserIncomingFriend(it)
            }
        }

        val outgoingFriendsResult = getUserOutgoingFriends()
        if (outgoingFriendsResult is DataResult.Error) {
            return outgoingFriendsResult
        }

        (outgoingFriendsResult as DataResult.Success).data.forEach {
            if (it.docRef == friendDocRef) {
                return DataErrorHandler.handle(FirebaseAlreadySentFriendException())
            }
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

    override suspend fun removeUserFriend(
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef =
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
                remoteDataSource.db.collection(
                    appContext.getString(
                        R.string.collection_name_users
                    )
                )
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
