package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.Trip
import com.example.spender.data.models.user.User
import com.example.spender.data.models.user.UserName

interface UserRepositoryInterface {
    suspend fun createUser(userID: String, nickname: String): FirebaseCallResult<User>
    suspend fun getUser(userID: String): FirebaseCallResult<User>
    suspend fun getUserName(userID: String): FirebaseCallResult<UserName>
    suspend fun getUserAge(userID: String): FirebaseCallResult<Int>
    suspend fun getUserNickname(userID: String): FirebaseCallResult<String>
    suspend fun getUserIncomingFriends(userID: String): FirebaseCallResult<List<Friend>>
    suspend fun getUserOutgoingFriends(userID: String): FirebaseCallResult<List<Friend>>
    suspend fun getUserFriends(userID: String): FirebaseCallResult<List<Friend>>
    suspend fun getUserAdminTrips(userID: String): FirebaseCallResult<List<Trip>>
    suspend fun getUserPassengerTrips(userID: String): FirebaseCallResult<List<Trip>>
    suspend fun updateUser(userID: String, newUser: User): FirebaseCallResult<String>
    suspend fun updateUserName(userID: String, newName: UserName): FirebaseCallResult<String>
    suspend fun updateUserAge(userID: String, newAge: Int): FirebaseCallResult<String>
    suspend fun updateUserNickname(userID: String, newNickname: String): FirebaseCallResult<String>
    suspend fun addUserIncomingFriend(userID: String, friend: Friend): FirebaseCallResult<String>
    suspend fun addUserOutgoingFriend(userID: String, friend: Friend): FirebaseCallResult<String>
    suspend fun removeUserFriend(userID: String, friend: Friend): FirebaseCallResult<String>
    suspend fun removeUserOutgoingFriend(userID: String, friend: Friend): FirebaseCallResult<String>
    suspend fun removeUserIncomingFriend(userID: String, friend: Friend): FirebaseCallResult<String>
    suspend fun checkNickname(nickname: String): FirebaseCallResult<Boolean>
}
