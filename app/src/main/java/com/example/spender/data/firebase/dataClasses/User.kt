package com.example.spender.data.firebase.dataClasses

import com.google.firebase.firestore.DocumentReference

data class User(
    val name: Triple<String, String, String>,
    val age: Int,
    val nickname: String,
    val incomingFriends: List<Friend>,
    val outgoingFriends: List<Friend>,
    val friends: List<Friend>,
    val trips: List<Trip>,
    val docRef: DocumentReference,
)