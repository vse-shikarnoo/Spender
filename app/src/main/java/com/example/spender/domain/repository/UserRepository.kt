package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName
import com.google.firebase.firestore.Source

interface UserRepository {
    suspend fun getUserName(source: Source): DataResult<UserName>
    suspend fun getUserAge(source: Source): DataResult<Long>
    suspend fun getUserNickname(source: Source): DataResult<String>
    suspend fun getUserIncomingFriends(source: Source): DataResult<List<Friend>>
    suspend fun getUserOutgoingFriends(source: Source): DataResult<List<Friend>>
    suspend fun getUserFriends(source: Source): DataResult<List<Friend>>
    suspend fun updateUserName(newName: UserName): DataResult<String>
    suspend fun updateUserAge(newAge: Int): DataResult<String>
    suspend fun updateUserNickname(newNickname: String): DataResult<String>
    suspend fun addUserIncomingFriend(friend: Friend): DataResult<String>
    suspend fun addUserOutgoingFriend(nickname: String): DataResult<String>
    suspend fun removeUserFriend(friend: Friend): DataResult<String>
    suspend fun removeUserOutgoingFriend(friend: Friend): DataResult<String>
    suspend fun removeUserIncomingFriend(friend: Friend): DataResult<String>
}
