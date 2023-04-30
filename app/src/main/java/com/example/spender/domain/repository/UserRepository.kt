package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName

interface UserRepository {
    suspend fun getUserName(): DataResult<UserName>
    suspend fun getUserAge(): DataResult<Long>
    suspend fun getUserNickname(): DataResult<String>
    suspend fun getUserIncomingFriends(): DataResult<List<Friend>>
    suspend fun getUserOutgoingFriends(): DataResult<List<Friend>>
    suspend fun getUserFriends(): DataResult<List<Friend>>
    suspend fun updateUserName(newName: UserName): DataResult<String>
    suspend fun updateUserAge(newAge: Int): DataResult<String>
    suspend fun updateUserNickname(newNickname: String): DataResult<String>
    suspend fun addUserIncomingFriend(friend: Friend): DataResult<String>
    suspend fun addUserOutgoingFriend(nickname: String): DataResult<String>
    suspend fun removeUserFriend(friend: Friend): DataResult<String>
    suspend fun removeUserOutgoingFriend(friend: Friend): DataResult<String>
    suspend fun removeUserIncomingFriend(friend: Friend): DataResult<String>
}
