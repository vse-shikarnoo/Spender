package com.example.spender.domain.remotedao

import com.example.spender.data.DataResult
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.google.firebase.firestore.Source

interface RemoteUserDao {
    var source: Source

    suspend fun getUser(userId: String? = null): DataResult<User>
    suspend fun getUserName(userId: String? = null): DataResult<UserName>
    suspend fun getUserAge(userId: String? = null): DataResult<Long>
    suspend fun getUserNickname(userId: String? = null): DataResult<String>
    suspend fun getUserIncomingFriends(userId: String? = null): DataResult<List<Friend>>
    suspend fun getUserOutgoingFriends(userId: String? = null): DataResult<List<Friend>>
    suspend fun getUserFriends(userId: String? = null): DataResult<List<Friend>>
    suspend fun getUserAdminTrips(userId: String? = null): DataResult<List<Trip>>
    suspend fun getUserPassengerTrips(userId: String? = null): DataResult<List<Trip>>
    suspend fun updateUser(userId: String? = null, newUser: User): DataResult<String>
    suspend fun updateUserName(userId: String? = null, newName: UserName): DataResult<String>
    suspend fun updateUserAge(userId: String? = null, newAge: Int): DataResult<String>
    suspend fun updateUserNickname(userId: String? = null, newNickname: String): DataResult<String>
    suspend fun addUserIncomingFriend(userId: String? = null, friend: Friend): DataResult<String>
    suspend fun addUserOutgoingFriend(userId: String? = null, friend: Friend): DataResult<String>
    suspend fun removeUserFriend(userId: String? = null, friend: Friend): DataResult<String>
    suspend fun removeUserOutgoingFriend(userId: String? = null, friend: Friend): DataResult<String>
    suspend fun removeUserIncomingFriend(userId: String? = null, friend: Friend): DataResult<String>
    suspend fun checkNickname(nickname: String): DataResult<Boolean>
}