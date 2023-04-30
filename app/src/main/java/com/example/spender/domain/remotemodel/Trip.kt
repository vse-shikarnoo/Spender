package com.example.spender.domain.remotemodel

import com.example.spender.domain.remotemodel.user.Friend
import com.google.firebase.firestore.DocumentReference

data class Trip(
    val name: String,
    val creator: Friend,
    val members: List<Friend>,
    // NOT USED val remoteSpends: List<RemoteSpend>,
    val docRef: DocumentReference
)
