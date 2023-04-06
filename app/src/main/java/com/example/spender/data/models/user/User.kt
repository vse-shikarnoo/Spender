package com.example.spender.data.models.user

import com.example.spender.data.models.Trip
import com.google.firebase.firestore.DocumentReference

data class User(
    val name: UserName,
    val age: Int,
    val nickname: String,
    val incomingFriends: List<Friend>,
    val outgoingFriends: List<Friend>,
    val friends: List<Friend>,
    val trips: List<Trip>,
    val docRef: DocumentReference,
)
