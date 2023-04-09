package com.example.spender.data.firebase

import com.example.spender.data.firebase.repositories.AuthRepository
import com.example.spender.data.firebase.repositories.SpendRepository
import com.example.spender.data.firebase.repositories.TripRepository
import com.example.spender.data.firebase.repositories.UserRepository

object FirebaseRepositoriesHolder {
    val authManagerRepository by lazy { AuthRepository() }
    val userRepository by lazy { UserRepository() }
    val tripRepository by lazy { TripRepository() }
    val spendRepository by lazy { SpendRepository() }
}