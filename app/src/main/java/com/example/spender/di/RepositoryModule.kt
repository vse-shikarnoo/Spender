package com.example.spender.di

import com.example.spender.data.firebase.repositoryInterfaces.AuthRepositoryInterface
import com.example.spender.data.firebase.repositories.AuthRepository
import com.example.spender.data.firebase.repositories.SpendRepository
import com.example.spender.data.firebase.repositories.TripRepository
import com.example.spender.data.firebase.repositories.UserRepository
import com.example.spender.data.firebase.repositoryInterfaces.SpendRepositoryInterface
import com.example.spender.data.firebase.repositoryInterfaces.TripRepositoryInterface
import com.example.spender.data.firebase.repositoryInterfaces.UserRepositoryInterface
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
        authRepository: AuthRepository
    ): AuthRepositoryInterface

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): UserRepositoryInterface

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepository: TripRepository
    ): TripRepositoryInterface
    
    @Binds
    @Singleton
    abstract fun bindSpendRepository(
        spendRepository: SpendRepository
    ): SpendRepositoryInterface
}