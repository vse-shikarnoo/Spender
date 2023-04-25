package com.example.spender.domain.model

import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.user.Friend
import com.google.firebase.firestore.DocumentReference

data class Trip(
    val name: String,
    val creator: Friend,
    val members: List<Friend>,
    val spends: List<Spend>,
    val docRef: DocumentReference
)

data class TestTrip(
    val name: String
)
