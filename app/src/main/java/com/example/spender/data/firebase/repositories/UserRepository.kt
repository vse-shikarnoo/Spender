package com.example.spender.data.firebase.repositories

import android.util.Log
import com.example.spender.data.firebase.*
import com.example.spender.data.firebase.databaseFieldNames.CollectionNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionTripDocumentFieldNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.interfaces.UserRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameLengthException
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

    override suspend fun getUser(
        userID: String
    ): FirebaseCallResult<User> {
        return try {
            val userDocumentSnapshot = userCollection.document(userID).get().await()

            val name = getUserName(userID)
            if (name is FirebaseCallResult.Error) {
                return name
            }
            val age = getUserAge(userID)
            if (age is FirebaseCallResult.Error) {
                return age
            }
            val nickname = getUserNickname(userID)
            if (nickname is FirebaseCallResult.Error) {
                return nickname
            }
            val incomingFriends = getUserIncomingFriends(userID)
            if (incomingFriends is FirebaseCallResult.Error) {
                return incomingFriends
            }
            val outgoingFriends = getUserOutgoingFriends(userID)
            if (outgoingFriends is FirebaseCallResult.Error) {
                return outgoingFriends
            }
            val friends = getUserFriends(userID)
            if (friends is FirebaseCallResult.Error) {
                return friends
            }
            val adminTrips = getUserAdminTrips(userID)
            if (adminTrips is FirebaseCallResult.Error) {
                return adminTrips
            }
            val passengerTrips = getUserPassengerTrips(userID)
            if (passengerTrips is FirebaseCallResult.Error) {
                return passengerTrips
            }
            val docRef = userDocumentSnapshot.reference

            FirebaseCallResult.Success(
                User(
                    name = (name as FirebaseCallResult.Success).data,
                    age = (age as FirebaseCallResult.Success).data,
                    nickname = (nickname as FirebaseCallResult.Success).data,
                    incomingFriends = (incomingFriends as FirebaseCallResult.Success).data,
                    outgoingFriends = (outgoingFriends as FirebaseCallResult.Success).data,
                    friends = (friends as FirebaseCallResult.Success).data,
                    adminTrips = (adminTrips as FirebaseCallResult.Success).data,
                    passengerTrips = (passengerTrips as FirebaseCallResult.Success).data,
                    docRef = docRef
                )
            )
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
    ): FirebaseCallResult<Long> {
        return try {
            val userDocRef = userCollection.document(userID)
            val age = userDocRef.get().await().data!![CollectionUserDocumentFieldNames.AGE] as Long
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
                .data!![CollectionUserDocumentFieldNames.INCOMING_FRIENDS] as ArrayList<DocumentReference>?

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
                        Log.d("ABOBA", "GetIncoming undefined")
                        return FirebaseErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                FirebaseCallResult.Success(lst)
            } else {
                FirebaseCallResult.Success(emptyList())
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
                .data!![CollectionUserDocumentFieldNames.OUTGOING_FRIENDS] as ArrayList<DocumentReference>?

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
                        Log.d("ABOBA", "GetOutgoing undefined")
                        return FirebaseErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                FirebaseCallResult.Success(lst)
            } else {
                FirebaseCallResult.Success(emptyList())
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
                .data!![CollectionUserDocumentFieldNames.FRIENDS] as ArrayList<DocumentReference>?

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
                        Log.d("ABOBA", "GetUserFriends undefined")
                        return FirebaseErrorHandler.handle(FirebaseUndefinedException())
                    }
                }

                FirebaseCallResult.Success(lst)
            } else {
                FirebaseCallResult.Success(emptyList())
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
                tripCollection.whereEqualTo(CollectionTripDocumentFieldNames.CREATOR, userID).get()
                    .await()
            if (!tripsQuery.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuery.documents.forEach { tripDocSnapshot ->
                    lst.add(tripDocSnapshot.toObject(Trip::class.java)!!)
                }
                FirebaseCallResult.Success(lst)
            } else {
                FirebaseCallResult.Success(emptyList())
            }
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
            val tripsQuery =
                tripCollection.whereNotEqualTo(CollectionTripDocumentFieldNames.CREATOR, userID)
            val tripsQuerySnapshot =
                tripsQuery.whereArrayContains(CollectionTripDocumentFieldNames.MEMBERS, userID)
                    .get().await()
            if (!tripsQuerySnapshot.isEmpty) {
                val lst: MutableList<Trip> = mutableListOf()
                tripsQuerySnapshot.documents.forEach { tripDocSnapshot ->
                    lst.add(tripDocSnapshot.toObject(Trip::class.java)!!)
                }
                FirebaseCallResult.Success(lst)
            } else {
                FirebaseCallResult.Success(emptyList())
            }
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
                    if (checkNicknameResult.data) {
                        val userDocRef = userCollection.document(userID)
                        userDocRef.update(CollectionUserDocumentFieldNames.NICKNAME, newNickname)
                            .await()
                        FirebaseCallResult.Success(FirebaseSuccessMessages.USER_NICKNAME_UPDATED)
                    } else {
                        FirebaseErrorHandler.handle(FirebaseNicknameException())
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
