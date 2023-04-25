package com.example.spender.di

import com.example.spender.domain.usecases.implementations.SpendUpdateUseCaseImpl
import com.example.spender.domain.usecases.interfaces.SpendUpdateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindSpendUpdateUseCase(
        spendUpdateUseCaseImpl: SpendUpdateUseCaseImpl
    ): SpendUpdateUseCase
}
