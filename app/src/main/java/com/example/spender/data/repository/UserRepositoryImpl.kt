package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteUserDaoImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.example.spender.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDaoImpl: RemoteUserDaoImpl
) : UserRepository {
    override suspend fun getUser(userId: String?): DataResult<User> {
        return remoteUserDaoImpl.getUser(userId)
    }

    override suspend fun getUserName(userId: String?): DataResult<UserName> {
        return remoteUserDaoImpl.getUserName(userId)
    }

    override suspend fun getUserAge(userId: String?): DataResult<Long> {
        return remoteUserDaoImpl.getUserAge(userId)
    }

    override suspend fun getUserNickname(userId: String?): DataResult<String> {
        return remoteUserDaoImpl.getUserNickname(userId)
    }

    override suspend fun getUserIncomingFriends(userId: String?): DataResult<List<Friend>> {
        return remoteUserDaoImpl.getUserIncomingFriends(userId)
    }

    override suspend fun getUserOutgoingFriends(userId: String?): DataResult<List<Friend>> {
        return remoteUserDaoImpl.getUserOutgoingFriends(userId)
    }

    override suspend fun getUserFriends(userId: String?): DataResult<List<Friend>> {
        return remoteUserDaoImpl.getUserFriends(userId)
    }

    override suspend fun getUserAdminTrips(userId: String?): DataResult<List<Trip>> {
        return remoteUserDaoImpl.getUserAdminTrips(userId)
    }

    override suspend fun getUserPassengerTrips(userId: String?): DataResult<List<Trip>> {
        return remoteUserDaoImpl.getUserPassengerTrips(userId)
    }

    override suspend fun updateUser(userId: String?, newUser: User): DataResult<String> {
        return remoteUserDaoImpl.updateUser(userId, newUser)
    }

    override suspend fun updateUserName(userId: String?, newName: UserName): DataResult<String> {
        return remoteUserDaoImpl.updateUserName(userId, newName)
    }

    override suspend fun updateUserAge(userId: String?, newAge: Int): DataResult<String> {
        return remoteUserDaoImpl.updateUserAge(userId, newAge)
    }

    override suspend fun updateUserNickname(
        userId: String?,
        newNickname: String
    ): DataResult<String> {
        return remoteUserDaoImpl.updateUserNickname(userId, newNickname)
    }

    override suspend fun addUserIncomingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImpl.addUserIncomingFriend(userId, friend)
    }

    override suspend fun addUserOutgoingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImpl.addUserOutgoingFriend(userId, friend)
    }

    override suspend fun removeUserFriend(userId: String?, friend: Friend): DataResult<String> {
        return remoteUserDaoImpl.removeUserFriend(userId, friend)
    }

    override suspend fun removeUserOutgoingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImpl.removeUserOutgoingFriend(userId, friend)
    }

    override suspend fun removeUserIncomingFriend(
        userId: String?,
        friend: Friend
    ): DataResult<String> {
        return remoteUserDaoImpl.removeUserIncomingFriend(userId, friend)
    }

    override suspend fun checkNickname(nickname: String): DataResult<Boolean> {
        return remoteUserDaoImpl.checkNickname(nickname)
    }

}