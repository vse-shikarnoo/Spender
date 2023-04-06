package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.*
import com.example.spender.data.firebase.databaseFieldNames.CollectionNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionTripDocumentFieldNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.interfaces.UserRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.messages.exceptions.FirebaseUndefinedException
import com.example.spender.data.models.Trip
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.user.User
import com.example.spender.data.models.user.UserName
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class UserRepository : UserRepositoryInterface {
    private val userCollection by lazy { FirebaseInstanceHolder.db.collection(CollectionNames.USER) }

    override suspend fun createUser(
        userID: String,
        nickname: String
    ): FirebaseCallResult<User> {
        return try {
            when (val checkNicknameResult = checkNickname(nickname)) {
                is FirebaseCallResult.Success -> {
                    userCollection.document(userID).set(
                        User(
                            name = UserName("", "", ""),
                            age = 0,
                            nickname = nickname,
                            incomingFriends = emptyList(),
                            outgoingFriends = emptyList(),
                            friends = emptyList(),
                            trips = emptyList(),
                            docRef = userCollection.document(userID)
                        )
                    ).await()
                    when (val newUserResult = getUser(userID)) {
                        is FirebaseCallResult.Success -> {
                            FirebaseCallResult.Success(newUserResult.data)
                        }
                        is FirebaseCallResult.Error -> {
                            newUserResult
                        }
                    }
                }
                is FirebaseCallResult.Error -> {
                    checkNicknameResult
                }
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    // Getters

    override suspend fun getUser(
        userID: String
    ): FirebaseCallResult<User> {
        return try {
            val user = userCollection.document(userID).get().await().toObject(User::class.java)
            FirebaseCallResult.Success(user!!)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserName(
        userID: String,
    ): FirebaseCallResult<UserName> {
        return try {
            val userCollection = FirebaseInstanceHolder.db.collection(CollectionNames.USER)
            val userDocRef = userCollection.document(userID)
            val firstName = userDocRef.get()
                .await().data!![CollectionUserDocumentFieldNames.FIRST_NAME] as String
            val middleName = userDocRef.get()
                .await().data!![CollectionUserDocumentFieldNames.MIDDLE_NAME] as String
            val lastName = userDocRef.get()
                .await().data!![CollectionUserDocumentFieldNames.LAST_NAME] as String
            FirebaseCallResult.Success(UserName(firstName, middleName, lastName))
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAge(
        userID: String,
    ): FirebaseCallResult<Int> {
        return try {
            val userDocRef = userCollection.document(userID)
            val age = userDocRef.get().await().data!![CollectionUserDocumentFieldNames.AGE] as Int
            FirebaseCallResult.Success(age)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserNickname(
        userID: String,
    ): FirebaseCallResult<String> {
        return try {
            val userCollection = FirebaseInstanceHolder.db.collection(CollectionNames.USER)
            val userDocRef = userCollection.document(userID)
            val nickname =
                userDocRef.get().await()
                    .data!![CollectionUserDocumentFieldNames.NICKNAME] as String
            FirebaseCallResult.Success(nickname)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserIncomingFriends(
        userID: String,
    ): FirebaseCallResult<List<Friend>> {
        return try {
            val userDocRef = userCollection.document(userID)
            val friends = userDocRef.get().await()
                .data!![CollectionUserDocumentFieldNames.INCOMING_FRIENDS] as Array<DocumentReference>?

            val lst = mutableListOf<Friend>()

            if (friends != null) {

                for (friend in friends) {
                    val resultName = getUserName(friend.id)
                    val resultNickname = getUserNickname(friend.id)
                    if (resultName is FirebaseCallResult.Success &&
                        resultNickname is FirebaseCallResult.Success
                    ) {
                        lst.add(
                            Friend(
                                name = resultName.data,
                                nickname = resultNickname.data,
                                docRef = friend
                            )
                        )
                    } else {
                        return FirebaseErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                FirebaseCallResult.Success(lst)
            } else {
                FirebaseErrorHandler.handle(FirebaseNoFriendsException())
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserOutgoingFriends(
        userID: String,
    ): FirebaseCallResult<List<Friend>> {
        return try {
            val userDocRef = userCollection.document(userID)
            val friends = userDocRef.get().await()
                .data!![CollectionUserDocumentFieldNames.OUTGOING_FRIENDS] as Array<DocumentReference>?

            val lst = mutableListOf<Friend>()

            if (friends != null) {

                for (friend in friends) {
                    val resultName = getUserName(friend.id)
                    val resultNickname = getUserNickname(friend.id)
                    if (resultName is FirebaseCallResult.Success &&
                        resultNickname is FirebaseCallResult.Success
                    ) {
                        lst.add(
                            Friend(
                                name = resultName.data,
                                nickname = resultNickname.data,
                                docRef = friend
                            )
                        )
                    } else {
                        return FirebaseErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                FirebaseCallResult.Success(lst)
            } else {
                FirebaseErrorHandler.handle(FirebaseNoFriendsException())
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserFriends(
        userID: String,
    ): FirebaseCallResult<List<Friend>> {
        return try {
            val userDocRef = userCollection.document(userID)
            val friends = userDocRef.get().await()
                .data!![CollectionUserDocumentFieldNames.FRIENDS] as Array<DocumentReference>?

            val lst = mutableListOf<Friend>()

            if (friends != null) {

                for (friend in friends) {
                    val resultName = getUserName(friend.id)
                    val resultNickname = getUserNickname(friend.id)
                    if (resultName is FirebaseCallResult.Success &&
                        resultNickname is FirebaseCallResult.Success
                    ) {
                        lst.add(
                            Friend(
                                name = resultName.data,
                                nickname = resultNickname.data,
                                docRef = friend
                            )
                        )
                    } else {
                        return FirebaseErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                FirebaseCallResult.Success(lst)
            } else {
                FirebaseErrorHandler.handle(FirebaseNoFriendsException())
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAdminTrips(
        userID: String
    ): FirebaseCallResult<List<Trip>> {
        return try {
            val tripCollection: CollectionReference =
                FirebaseInstanceHolder.db.collection(CollectionNames.TRIP)
            val tripsQuery =
                tripCollection.whereEqualTo(CollectionTripDocumentFieldNames.CREATOR, userID)
            val lst: MutableList<Trip> = mutableListOf()
            tripsQuery.get().await().documents.forEach { tripDocSnapshot ->
                lst.add(tripDocSnapshot.toObject(Trip::class.java)!!)
            }
            FirebaseCallResult.Success(lst)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserPassengerTrips(
        userID: String
    ): FirebaseCallResult<List<Trip>> {
        return try {
            val tripCollection: CollectionReference =
                FirebaseInstanceHolder.db.collection(CollectionNames.TRIP)
            var tripsQuery =
                tripCollection.whereNotEqualTo(CollectionTripDocumentFieldNames.CREATOR, userID)
            tripsQuery =
                tripsQuery.whereArrayContains(CollectionTripDocumentFieldNames.MEMBERS, userID)
            val lst: MutableList<Trip> = mutableListOf()
            tripsQuery.get().await().documents.forEach { tripDocSnapshot ->
                lst.add(tripDocSnapshot.toObject(Trip::class.java)!!)
            }
            FirebaseCallResult.Success(lst)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUser(
        userID: String, newUser: User
    ): FirebaseCallResult<String> {
        return try {
            userCollection.document(userID).set(newUser).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserName(
        userID: String,
        newName: UserName,
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            userDocRef.update(CollectionUserDocumentFieldNames.FIRST_NAME, newName.firstName)
                .await()
            userDocRef.update(CollectionUserDocumentFieldNames.MIDDLE_NAME, newName.middleName)
                .await()
            userDocRef.update(CollectionUserDocumentFieldNames.LAST_NAME, newName.lastName).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_NAME_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserAge(
        userID: String,
        newAge: Int,
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            userDocRef.update(CollectionUserDocumentFieldNames.AGE, newAge).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_AGE_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserNickname(
        userID: String,
        newNickname: String,
    ): FirebaseCallResult<String> {
        return try {
            when (val checkNicknameResult = checkNickname(newNickname)) {
                is FirebaseCallResult.Success -> {
                    val userDocRef = userCollection.document(userID)
                    userDocRef.update(CollectionUserDocumentFieldNames.NICKNAME, newNickname).await()
                    FirebaseCallResult.Success(FirebaseSuccessMessages.USER_NICKNAME_UPDATED)
                }
                is FirebaseCallResult.Error -> {
                    checkNicknameResult
                }
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun addUserIncomingFriend(
        userID: String,
        friend: Friend,
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = FirebaseInstanceHolder.db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayUnion(friend.docRef)
            )
            batch.update(
                friend.docRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayUnion(userDocRef)
            )

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }


    override suspend fun addUserOutgoingFriend(
        userID: String,
        friend: Friend,
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = FirebaseInstanceHolder.db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayUnion(friend.docRef)
            )

            batch.update(
                friend.docRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayUnion(userDocRef)
            )

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserFriend(
        userID: String,
        friend: Friend,
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = FirebaseInstanceHolder.db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_FRIEND_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserOutgoingFriend(
        userID: String,
        friend: Friend
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = FirebaseInstanceHolder.db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserIncomingFriend(
        userID: String,
        friend: Friend
    ): FirebaseCallResult<String> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = FirebaseInstanceHolder.db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun checkNickname(
        nickname: String
    ): FirebaseCallResult<Boolean> {
        return try {
            val checkNicknameQuerySnapshot =
                userCollection.whereEqualTo(CollectionUserDocumentFieldNames.NICKNAME, nickname)
                    .get().await()
            FirebaseCallResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
