package com.example.spender.data.firebase.repositoryInterfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.Trip
import com.example.spender.data.models.user.User
import com.example.spender.data.models.user.UserName

interface UserRepositoryInterface {
    suspend fun getUser(userId: String? = null): FirebaseCallResult<User>
    suspend fun getUserName(userId: String? = null): FirebaseCallResult<UserName>
    suspend fun getUserAge(userId: String? = null): FirebaseCallResult<Long>
    suspend fun getUserNickname(userId: String? = null): FirebaseCallResult<String>
    suspend fun getUserIncomingFriends(userId: String? = null): FirebaseCallResult<List<Friend>>
    suspend fun getUserOutgoingFriends(userId: String? = null): FirebaseCallResult<List<Friend>>
    suspend fun getUserFriends(userId: String? = null): FirebaseCallResult<List<Friend>>
    suspend fun getUserAdminTrips(userId: String? = null): FirebaseCallResult<List<Trip>>
    suspend fun getUserPassengerTrips(userId: String? = null): FirebaseCallResult<List<Trip>>
    suspend fun updateUser(userId: String? = null, newUser: User): FirebaseCallResult<String>
    suspend fun updateUserName(userId: String? = null, newName: UserName): FirebaseCallResult<String>
    suspend fun updateUserAge(userId: String? = null, newAge: Int): FirebaseCallResult<String>
    suspend fun updateUserNickname(userId: String? = null, newNickname: String): FirebaseCallResult<String>
    suspend fun addUserIncomingFriend(userId: String? = null, friend: Friend): FirebaseCallResult<String>
    suspend fun addUserOutgoingFriend(userId: String? = null, friend: Friend): FirebaseCallResult<String>
    suspend fun removeUserFriend(userId: String? = null, friend: Friend): FirebaseCallResult<String>
    suspend fun removeUserOutgoingFriend(userId: String? = null, friend: Friend): FirebaseCallResult<String>
    suspend fun removeUserIncomingFriend(userId: String? = null, friend: Friend): FirebaseCallResult<String>
    suspend fun checkNickname(nickname: String): FirebaseCallResult<Boolean>
}
