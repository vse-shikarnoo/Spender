package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.dataClasses.Friend
import com.example.spender.data.firebase.dataClasses.Trip
import com.example.spender.data.firebase.dataClasses.User
import com.example.spender.data.firebase.fieldNames.CollectionUserDocumentFieldNames
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

interface UserRepositoryInterface {
    suspend fun createUser(userID: String): Result<Boolean>
    suspend fun getUser(userID: String): Result<User>
    suspend fun getUserName(userID: String): Result<Triple<String, String, String>>
    suspend fun getUserAge(userID: String): Result<Int>
    suspend fun getUserNickname(userID: String): Result<String>
    suspend fun getUserFriends(userID: String): Result<List<Friend>>
    suspend fun updateUserName(userID: String, newName: String): Result<Boolean>
    suspend fun updateUserAge(userID: String, age: Int): Result<Boolean>
    suspend fun updateUserNickname(userID: String, nickname: String): Result<Boolean>
    suspend fun addUserOutgoingFriend(
        userID: String,
        friendDocRef: DocumentReference
    ): Result<Boolean>

    suspend fun addUserIncomingFriend(
        userID: String,
        friendDocRef: DocumentReference
    ): Result<Boolean>

    suspend fun removeUserFriend(userID: String, friendDocRef: DocumentReference): Result<Boolean>
    suspend fun removeUserFriends(
        userID: String,
        friendDocRefs: List<DocumentReference>
    ): Result<Boolean>

    suspend fun removeUserOutgoingFriend(
        userID: String,
        friendDocRef: DocumentReference
    ): Result<Boolean>

    suspend fun removeUserIncomingFriend(
        userID: String,
        friendDocRef: DocumentReference
    ): Result<Boolean>

    companion object {
        suspend fun getUserNickname(
            userID: String,
            userCollection: CollectionReference
        ): Result<String> {
            return try {
                val userDocRef = userCollection.document(userID)
                val nickname =
                    userDocRef.get().await().data!![CollectionUserDocumentFieldNames.NICKNAME] as String
                Result.Success(nickname)
            } catch (e: Exception) {
                when (e) {
                    is FirebaseNetworkException -> Result.Error("Network error")
                    else -> Result.Error("Unknown error")
                }
            }
        }
        suspend fun getUserName(
            userID: String,
            userCollection: CollectionReference
        ): Result<Triple<String, String, String>> {
            return try {
                val userDocRef = userCollection.document(userID)
                val firstName = userDocRef.get()
                    .await().data!![CollectionUserDocumentFieldNames.FIRST_NAME] as String
                val middleName = userDocRef.get()
                    .await().data!![CollectionUserDocumentFieldNames.MIDDLE_NAME] as String
                val lastName = userDocRef.get()
                    .await().data!![CollectionUserDocumentFieldNames.LAST_NAME] as String
                Result.Success(Triple(firstName, middleName, lastName))
            } catch (e: Exception) {
                when (e) {
                    is FirebaseNetworkException -> Result.Error("Network error")
                    else -> Result.Error("Unknown error")
                }
            }
        }
    }
}