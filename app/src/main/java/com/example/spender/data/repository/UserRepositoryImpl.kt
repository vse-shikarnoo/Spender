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

    override suspend fun getUserName(source: Source): DataResult<UserName> {
        return if (source == Source.CACHE) {
            remoteUserDaoImplCache.getUserName()
        } else {
            remoteUserDaoImplServer.getUserName()
        }
    }

    override suspend fun getUserAge(source: Source): DataResult<Long> {
        return if (source == Source.CACHE) {
            remoteUserDaoImplCache.getUserAge()
        } else {
            remoteUserDaoImplServer.getUserAge()
        }
    }

    override suspend fun getUserNickname(source: Source): DataResult<String> {
        return if (source == Source.CACHE) {
            remoteUserDaoImplCache.getUserNickname()
        } else {
            remoteUserDaoImplServer.getUserNickname()
        }
    }

    override suspend fun getUserIncomingFriends(source: Source): DataResult<List<Friend>> {
        return if (source == Source.CACHE) {
            remoteUserDaoImplCache.getUserIncomingFriends()
        } else {
            remoteUserDaoImplServer.getUserIncomingFriends()
        }
    }

    override suspend fun getUserOutgoingFriends(source: Source): DataResult<List<Friend>> {
        return if (source == Source.CACHE) {
            remoteUserDaoImplCache.getUserOutgoingFriends()
        } else {
            remoteUserDaoImplServer.getUserOutgoingFriends()
        }
    }

    override suspend fun getUserFriends(source: Source): DataResult<List<Friend>> {
        return if (source == Source.CACHE) {
            remoteUserDaoImplCache.getUserFriends()
        } else {
            remoteUserDaoImplServer.getUserFriends()
        }
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
