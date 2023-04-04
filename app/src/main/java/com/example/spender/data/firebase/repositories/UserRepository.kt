package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.databaseFieldNames.CollectionNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.interfaces.UserRepositoryInterface
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository : UserRepositoryInterface {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val userCollection by lazy { db.collection(CollectionNames.USER) }

    // Creators

    override suspend fun createUser(
        userID: String,
        nickname: String
    ): Result<Boolean> {
        return try {
            userCollection.document(userID).set(
                User(
                    name = Triple("", "", ""),
                    age = 0,
                    nickname = nickname,
                    incomingFriends = emptyList(),
                    outgoingFriends = emptyList(),
                    friends = emptyList(),
                    trips = emptyList(),
                    docRef = userCollection.document(userID)
                )
            ).await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    // Getters

    override suspend fun getUser(
        userID: String
    ): Result<User> {
        return try {
            val user = userCollection.document(userID).get().await().toObject(User::class.java)
            Result.Success(user!!)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getUserName(
        userID: String,
    ): Result<Triple<String, String, String>> {
        return when (val result = UserRepositoryInterface.getUserName(userID, userCollection)) {
            is Result.Success -> result
            is Result.Error -> result
        }
    }

    override suspend fun getUserAge(
        userID: String,
    ): Result<Int> {
        return try {
            val userDocRef = userCollection.document(userID)
            val age = userDocRef.get().await().data!![CollectionUserDocumentFieldNames.AGE] as Int
            Result.Success(age)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getUserNickname(
        userID: String,
    ): Result<String> {
        return when (val result = UserRepositoryInterface.getUserNickname(userID, userCollection)) {
            is Result.Success -> result
            is Result.Error -> result
        }
    }

    override suspend fun getUserFriends(
        userID: String,
    ): Result<List<Friend>> {
        return try {
            val userDocRef = userCollection.document(userID)
            val friends = userDocRef.get().await()
                .data!![CollectionUserDocumentFieldNames.FRIENDS] as Array<DocumentReference>?
            if (friends != null) {
                val lst = mutableListOf<Friend>()
                for (friend in friends) {
                    when (val resultName = getUserName(friend.id)) {
                        is Result.Success -> {
                            when (val resultNickname = getUserNickname(friend.id)) {
                                is Result.Success -> {
                                    lst.add(
                                        Friend(
                                            name = resultName.data,
                                            nickname = resultNickname.data,
                                            docRef = friend
                                        )
                                    )
                                }
                                is Result.Error -> {
                                    Result.Error(resultNickname.exception)
                                }
                            }
                        }
                        is Result.Error -> {
                            Result.Error(resultName.exception)
                        }
                    }
                }
                Result.Success(lst)
            } else {
                Result.Error("No friends")
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    // Updates

    override suspend fun updateUserName(
        userID: String,
        newName: String,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            userDocRef.update(CollectionUserDocumentFieldNames.NAME, newName).await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun updateUserAge(
        userID: String,
        age: Int,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            userDocRef.update(CollectionUserDocumentFieldNames.AGE, age).await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun updateUserNickname(
        userID: String,
        nickname: String,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            userDocRef.update(CollectionUserDocumentFieldNames.NICKNAME, nickname).await()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun addUserOutgoingFriend(
        userID: String,
        friendDocRef: DocumentReference,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayUnion(friendDocRef)
            )

            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayUnion(userDocRef)
            )

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun addUserIncomingFriend(
        userID: String,
        friendDocRef: DocumentReference,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayUnion(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayUnion(userDocRef)
            )

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    // Deletes

    override suspend fun removeUserFriend(
        userID: String,
        friendDocRef: DocumentReference,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayRemove(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun removeUserFriends(
        userID: String,
        friendDocRefs: List<DocumentReference>,
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = db.batch()

            friendDocRefs.forEach { friendDocRef ->
                batch.update(
                    userDocRef,
                    CollectionUserDocumentFieldNames.FRIENDS,
                    FieldValue.arrayRemove(friendDocRef)
                )
                batch.update(
                    friendDocRef,
                    CollectionUserDocumentFieldNames.FRIENDS,
                    FieldValue.arrayRemove(userDocRef)
                )
            }

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun removeUserOutgoingFriend(
        userID: String,
        friendDocRef: DocumentReference
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun removeUserIncomingFriend(
        userID: String,
        friendDocRef: DocumentReference
    ): Result<Boolean> {
        return try {
            val userDocRef = userCollection.document(userID)
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }
}
