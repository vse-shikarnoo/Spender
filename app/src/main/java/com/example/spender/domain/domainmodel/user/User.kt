package com.example.spender.domain.domainmodel.user

import com.example.spender.domain.domainmodel.Trip
import com.google.firebase.firestore.DocumentReference

data class User(
    val name: UserName,
    val age: Long,
    val nickname: String,
    val incomingFriends: List<Friend>,
    val outgoingFriends: List<Friend>,
    val friends: List<Friend>,
    val adminTrips: List<Trip>,
    val passengerTrips: List<Trip>,
    val docRef: DocumentReference,
)
