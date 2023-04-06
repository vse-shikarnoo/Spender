package com.example.spender.data.models

import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.user.Friend
import com.google.firebase.firestore.DocumentReference

data class Trip(
    val name: String,
    val creator: Friend,
    val members: List<Friend>,
    val spends: List<Spend>,
    val docRef: DocumentReference
)
