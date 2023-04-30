package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteUserDaoImpl
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName
import com.example.spender.domain.repository.UserRepository
import com.google.firebase.firestore.Source
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDaoImplServer: RemoteUserDaoImpl,
    private val remoteUserDaoImplCache: RemoteUserDaoImpl
) : UserRepository {
    init {
        remoteUserDaoImplServer.source = Source.SERVER
        remoteUserDaoImplCache.source = Source.CACHE
    }

    override suspend fun getUserName(): DataResult<UserName> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserName()
        }
        return remoteUserDaoImplCache.getUserName()
    }

    override suspend fun getUserAge(): DataResult<Long> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserAge()
        }
        return remoteUserDaoImplCache.getUserAge()
    }

    override suspend fun getUserNickname(): DataResult<String> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserNickname()
        }
        return remoteUserDaoImplCache.getUserNickname()
    }

    override suspend fun getUserIncomingFriends(): DataResult<List<Friend>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserIncomingFriends()
        }
        return remoteUserDaoImplCache.getUserIncomingFriends()
    }

    override suspend fun getUserOutgoingFriends(): DataResult<List<Friend>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserOutgoingFriends()
        }
        return remoteUserDaoImplCache.getUserOutgoingFriends()
    }

    override suspend fun getUserFriends(): DataResult<List<Friend>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserFriends()
        }
        return remoteUserDaoImplCache.getUserFriends()
    }

    override suspend fun updateUserName(newName: UserName): DataResult<String> {
        return remoteUserDaoImplServer.updateUserName(newName)
    }

    override suspend fun updateUserAge(newAge: Int): DataResult<String> {
        return remoteUserDaoImplServer.updateUserAge(newAge)
    }

    override suspend fun updateUserNickname(
        newNickname: String
    ): DataResult<String> {
        return remoteUserDaoImplServer.updateUserNickname(newNickname)
    }

    override suspend fun addUserIncomingFriend(
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.addUserIncomingFriend(friend)
    }

    override suspend fun addUserOutgoingFriend(
        nickname: String
    ): DataResult<String> {
        return remoteUserDaoImplServer.addUserOutgoingFriend(nickname)
    }

    override suspend fun removeUserFriend(friend: Friend): DataResult<String> {
        return remoteUserDaoImplServer.removeUserFriend(friend)
    }

    override suspend fun removeUserOutgoingFriend(
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.removeUserOutgoingFriend(friend)
    }

    override suspend fun removeUserIncomingFriend(
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.removeUserIncomingFriend(friend)
    }
}
