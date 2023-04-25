package com.example.spender.data.remote

import com.example.spender.domain.RemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() :
    RemoteDataSource<FirebaseFirestore, FirebaseAuth> {
    override val db by lazy { FirebaseFirestore.getInstance() }
    override val auth by lazy { FirebaseAuth.getInstance() }

    companion object {
        @Volatile
        private var instance: RemoteDataSourceImpl? = null

        @Synchronized
        fun getInstance(): RemoteDataSourceImpl {
            if (instance == null) {
                instance = RemoteDataSourceImpl()
            }
            return instance as RemoteDataSourceImpl
        }
    }
}
