package com.example.spender.di

import com.example.spender.domain.remotedao.RemoteAuthDao
import com.example.spender.data.remote.dao.RemoteAuthDaoImpl
import com.example.spender.data.remote.dao.RemoteSpendDaoImpl
import com.example.spender.data.remote.dao.RemoteTripDaoImpl
import com.example.spender.data.remote.dao.RemoteUserDaoImpl
import com.example.spender.domain.remotedao.RemoteSpendDao
import com.example.spender.domain.remotedao.RemoteTripDao
import com.example.spender.domain.remotedao.RemoteUserDao
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
    ): RemoteAuthDao

    @Binds
    @Singleton
    abstract fun bindRemoteTripDao(
        remoteTripDaoImpl: RemoteTripDaoImpl
    ): RemoteTripDao

    @Binds
    @Singleton
    abstract fun bindRemoteUserDao(
        remoteUserDaoImpl: RemoteUserDaoImpl
    ): RemoteUserDao

    @Binds
    @Singleton
    abstract fun bindRemoteSpendDao(
        remoteSpendDaoImpl: RemoteSpendDaoImpl
    ): RemoteSpendDao
}