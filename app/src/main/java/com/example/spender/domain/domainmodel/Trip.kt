package com.example.spender.domain.domainmodel

import com.example.spender.domain.domainmodel.spend.Spend
import com.example.spender.domain.domainmodel.user.Friend
import com.google.firebase.firestore.DocumentReference

data class Trip(
    val name: String,
    val creator: Friend,
    val members: List<Friend>,
    val spends: List<Spend>,
    val docRef: DocumentReference
)
