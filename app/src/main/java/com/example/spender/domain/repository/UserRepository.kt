package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName

interface UserRepository {
    suspend fun getUser(): DataResult<User>
    suspend fun getUserName(userId: String? = null): DataResult<UserName>
    suspend fun getUserAge(): DataResult<Long>
    suspend fun getUserNickname(userId: String? = null): DataResult<String>
    suspend fun getUserIncomingFriends(): DataResult<List<Friend>>
    suspend fun getUserOutgoingFriends(): DataResult<List<Friend>>
    suspend fun getUserFriends(): DataResult<List<Friend>>
    suspend fun getUserTrips(): DataResult<List<Trip>>
    suspend fun getUserAdminTrips(): DataResult<List<Trip>>
    suspend fun getUserPassengerTrips(): DataResult<List<Trip>>
    suspend fun updateUser(newUser: User): DataResult<String>
    suspend fun updateUserName(newName: UserName): DataResult<String>
    suspend fun updateUserAge(newAge: Int): DataResult<String>
    suspend fun updateUserNickname(newNickname: String): DataResult<String>
    suspend fun addUserIncomingFriend(friend: Friend): DataResult<String>
    suspend fun addUserOutgoingFriend(friend: Friend): DataResult<String>
    suspend fun removeUserFriend(friend: Friend): DataResult<String>
    suspend fun removeUserOutgoingFriend(friend: Friend): DataResult<String>
    suspend fun removeUserIncomingFriend(friend: Friend): DataResult<String>
    suspend fun checkNickname(nickname: String): DataResult<Boolean>
}
