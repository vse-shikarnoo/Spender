package com.example.spender.di

import com.example.spender.domain.dao.AuthDao
import com.example.spender.data.remote.dao.RemoteAuthDaoImpl
import com.example.spender.data.remote.dao.RemoteSpendDaoImpl
import com.example.spender.data.remote.dao.RemoteTripDaoImpl
import com.example.spender.data.remote.dao.RemoteUserDaoImpl
import com.example.spender.domain.dao.SpendDao
import com.example.spender.domain.dao.TripDao
import com.example.spender.domain.dao.UserDao
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDaoModule {

    @Binds
    @Singleton
    abstract fun bindRemoteAuthDao(
        remoteAuthDaoImpl: RemoteAuthDaoImpl
    ): AuthDao

    @Binds
    @Singleton
    abstract fun bindRemoteTripDao(
        remoteTripDaoImpl: RemoteTripDaoImpl
    ): TripDao

    @Binds
    @Singleton
    abstract fun bindRemoteUserDao(
        remoteUserDaoImpl: RemoteUserDaoImpl
    ): UserDao

    @Binds
    @Singleton
    abstract fun bindRemoteSpendDao(
        remoteSpendDaoImpl: RemoteSpendDaoImpl
    ): SpendDao
}