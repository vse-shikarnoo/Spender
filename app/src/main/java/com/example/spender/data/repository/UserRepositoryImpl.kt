package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteUserDaoImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.example.spender.domain.repository.UserRepository
import com.google.firebase.firestore.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDaoImplServer: RemoteUserDaoImpl,
    private val remoteUserDaoImplCache: RemoteUserDaoImpl
) : UserRepository {
    init {
        remoteUserDaoImplServer.source = Source.SERVER
        remoteUserDaoImplCache.source = Source.CACHE
    }

    override suspend fun getUser(userId: String?): DataResult<User> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUser(userId)
        }
        return remoteUserDaoImplCache.getUser(userId)
    }

    override suspend fun getUserName(userId: String?): DataResult<UserName> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserName(userId)
        }
        return remoteUserDaoImplCache.getUserName(userId)
    }

    override suspend fun getUserAge(userId: String?): DataResult<Long> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserAge(userId)
        }
        return remoteUserDaoImplCache.getUserAge(userId)
    }

    override suspend fun getUserNickname(userId: String?): DataResult<String> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserNickname(userId)
        }
        return remoteUserDaoImplCache.getUserNickname(userId)
    }

    override suspend fun getUserIncomingFriends(userId: String?): DataResult<List<Friend>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserIncomingFriends(userId)
        }
        return remoteUserDaoImplCache.getUserIncomingFriends(userId)
    }

    override suspend fun getUserOutgoingFriends(userId: String?): DataResult<List<Friend>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserOutgoingFriends(userId)
        }
        return remoteUserDaoImplCache.getUserOutgoingFriends(userId)
    }

    override suspend fun getUserFriends(userId: String?): DataResult<List<Friend>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserFriends(userId)
        }
        return remoteUserDaoImplCache.getUserFriends(userId)
    }

    override suspend fun getUserAdminTrips(userId: String?): DataResult<List<Trip>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserAdminTrips(userId)
        }
        return remoteUserDaoImplCache.getUserAdminTrips(userId)
    }

    override suspend fun getUserPassengerTrips(userId: String?): DataResult<List<Trip>> {
        withContext(Dispatchers.IO) {
            remoteUserDaoImplServer.getUserPassengerTrips(userId)
        }
        return remoteUserDaoImplCache.getUserPassengerTrips(userId)
    }

    override suspend fun updateUser(userId: String?, newUser: User): DataResult<String> {
        return remoteUserDaoImplServer.updateUser(userId, newUser)
    }

    override suspend fun updateUserName(userId: String?, newName: UserName): DataResult<String> {
        return remoteUserDaoImplServer.updateUserName(userId, newName)
    }

    override suspend fun updateUserAge(userId: String?, newAge: Int): DataResult<String> {
        return remoteUserDaoImplServer.updateUserAge(userId, newAge)
    }

    override suspend fun updateUserNickname(
        userId: String?,
        newNickname: String
    ): DataResult<String> {
        return remoteUserDaoImplServer.updateUserNickname(userId, newNickname)
    }

    override suspend fun addUserIncomingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.addUserIncomingFriend(userId, friend)
    }

    override suspend fun addUserOutgoingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.addUserOutgoingFriend(userId, friend)
    }

    override suspend fun removeUserFriend(userId: String?, friend: Friend): DataResult<String> {
        return remoteUserDaoImplServer.removeUserFriend(userId, friend)
    }

    override suspend fun removeUserOutgoingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.removeUserOutgoingFriend(userId, friend)
    }

    override suspend fun removeUserIncomingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImplServer.removeUserIncomingFriend(userId, friend)
    }

    override suspend fun checkNickname(nickname: String): DataResult<Boolean> {
        return remoteUserDaoImplServer.checkNickname(nickname)
    }

}