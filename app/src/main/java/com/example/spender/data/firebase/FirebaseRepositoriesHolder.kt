package com.example.spender.data.firebase

import com.example.spender.data.firebase.repositories.AuthManagerRepository
import com.example.spender.data.firebase.repositories.SpendRepository
import com.example.spender.data.firebase.repositories.TripRepository
import com.example.spender.data.firebase.repositories.UserRepository

object FirebaseRepositoriesHolder {
    val authManagerRepository by lazy { AuthManagerRepository() }
    val userRepository by lazy { UserRepository() }
    val tripRepository by lazy { TripRepository() }
    val spendRepository by lazy { SpendRepository() }
}