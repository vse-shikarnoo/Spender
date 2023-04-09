package com.example.spender.data.firebase.repositories

import android.app.Application
import android.util.Log
import com.example.spender.R
import com.example.spender.data.firebase.*
import com.example.spender.data.firebase.repositoryInterfaces.UserRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.firebase.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.firebase.messages.exceptions.FirebaseUndefinedException
import com.example.spender.data.models.Trip
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.user.User
import com.example.spender.data.models.user.UserName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val appContext: Application
) : UserRepositoryInterface {
    override suspend fun getUser(
        userId: String?
    ): FirebaseCallResult<User> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()

            val userDocumentSnapshot = db.collection(appContext.getString(R.string.collection_name_users)).document(userID).get().await()

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
        userId: String?
    ): FirebaseCallResult<UserName> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val firstName = userDocRef.get()
                .await().data!![appContext.getString(R.string.collection_users_document_field_first_name)] as String
            val middleName = userDocRef.get()
                .await().data!![appContext.getString(R.string.collection_users_document_field_middle_name)] as String
            val lastName = userDocRef.get()
                .await().data!![appContext.getString(R.string.collection_users_document_field_last_name)] as String
            FirebaseCallResult.Success(UserName(firstName, middleName, lastName))
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserAge(
        userId: String?
    ): FirebaseCallResult<Long> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val age = userDocRef.get().await().data!![appContext.getString(R.string.collection_users_document_field_age)] as Long
            FirebaseCallResult.Success(age)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserNickname(
        userId: String?
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val nickname =
                userDocRef.get().await()
                    .data!![appContext.getString(R.string.collection_users_document_field_nickname)] as String
            FirebaseCallResult.Success(nickname)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun getUserIncomingFriends(
        userId: String?
    ): FirebaseCallResult<List<Friend>> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val friends = userDocRef.get().await()
                .data!![appContext.getString(R.string.collection_users_document_field_incoming_friends)] as ArrayList<DocumentReference>?

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
        userId: String?
    ): FirebaseCallResult<List<Friend>> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val friends = userDocRef.get().await()
                .data!![appContext.getString(R.string.collection_users_document_field_outgoing_friends)] as ArrayList<DocumentReference>?

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
        userId: String?
    ): FirebaseCallResult<List<Friend>> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val friends = userDocRef.get().await()
                .data!![appContext.getString(R.string.collection_users_document_field_friends)] as ArrayList<DocumentReference>?

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
        userId: String?
    ): FirebaseCallResult<List<Trip>> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val tripCollection: CollectionReference =
                db.collection(appContext.getString(R.string.collection_name_trips))
            val tripsQuery =
                tripCollection.whereEqualTo(appContext.getString(R.string.collection_trip_document_field_creator), userID).get()
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
        userId: String?
    ): FirebaseCallResult<List<Trip>> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val tripCollection: CollectionReference =
                db.collection(appContext.getString(R.string.collection_name_trips))
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
                FirebaseCallResult.Success(lst)
            } else {
                FirebaseCallResult.Success(emptyList())
            }
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUser(
        userId: String?,
        newUser: User
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            db.collection(appContext.getString(R.string.collection_name_users)).document(userID).set(newUser).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserName(
        userId: String?,
        newName: UserName
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_first_name), newName.firstName)
                .await()
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_middle_name), newName.middleName)
                .await()
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_last_name), newName.lastName).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_NAME_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserAge(
        userId: String?,
        newAge: Int,
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            userDocRef.update(appContext.getString(R.string.collection_users_document_field_age), newAge).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_AGE_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateUserNickname(
        userId: String?,
        newNickname: String,
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            when (val checkNicknameResult = checkNickname(newNickname)) {
                is FirebaseCallResult.Success -> {
                    if (checkNicknameResult.data) {
                        val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
                        userDocRef.update(appContext.getString(R.string.collection_users_document_field_nickname), newNickname)
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
        userId: String?,
        friend: Friend,
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val batch = db.batch()

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
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }


    override suspend fun addUserOutgoingFriend(
        userId: String?,
        friend: Friend,
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val batch = db.batch()

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
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserFriend(
        userId: String?,
        friend: Friend,
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val batch = db.batch()

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
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_FRIEND_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserOutgoingFriend(
        userId: String?,
        friend: Friend
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val batch = db.batch()

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
            FirebaseCallResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserIncomingFriend(
        userId: String?,
        friend: Friend
    ): FirebaseCallResult<String> {
        return try {
            val userID = userId ?: auth.currentUser?.uid.toString()
            val userDocRef = db.collection(appContext.getString(R.string.collection_name_users)).document(userID)
            val batch = db.batch()

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
                db.collection(appContext.getString(R.string.collection_name_users))
                    .whereEqualTo(appContext.getString(R.string.collection_users_document_field_nickname), nickname)
                    .get().await()
            FirebaseCallResult.Success(checkNicknameQuerySnapshot.isEmpty)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
