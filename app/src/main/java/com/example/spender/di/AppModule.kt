package com.example.spender.di

import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.RemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDataSource(): RemoteDataSource<FirebaseFirestore, FirebaseAuth> {
        return RemoteDataSourceImpl.getInstance()
    }
}
