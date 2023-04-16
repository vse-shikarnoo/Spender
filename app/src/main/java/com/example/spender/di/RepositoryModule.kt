package com.example.spender.di

import com.example.spender.domain.repository.AuthRepository
import com.example.spender.data.repository.AuthRepositoryImpl
import com.example.spender.data.repository.SpendRepositoryImpl
import com.example.spender.data.repository.TripRepositoryImpl
import com.example.spender.data.repository.UserRepositoryImpl
import com.example.spender.domain.repository.SpendRepository
import com.example.spender.domain.repository.TripRepository
import com.example.spender.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository
    
    @Binds
    @Singleton
    abstract fun bindSpendRepository(
        spendRepositoryImpl: SpendRepositoryImpl
    ): SpendRepository
}