package com.example.spender.domain

interface RemoteDataSource<D, A> {
    val db: D
    val auth: A
}
