package com.example.spender.domain.remotedao

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source

interface RemoteUserDao {
    var source: Source

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
    suspend fun checkExistNickname(nickname: String): DataResult<DocumentReference>
    suspend fun checkAlreadyFriend(friendDocRef: DocumentReference): DataResult<Boolean>
    suspend fun checkAlreadyIncoming(friendDocRef: DocumentReference): DataResult<Friend?>
    suspend fun checkAlreadyOutgoing(friendDocRef: DocumentReference): DataResult<Boolean>
    suspend fun removeUserFriend(friend: Friend): DataResult<String>
    suspend fun removeUserOutgoingFriend(friend: Friend): DataResult<String>
    suspend fun removeUserIncomingFriend(friend: Friend): DataResult<String>
    suspend fun checkNickname(nickname: String): DataResult<Boolean>
}
