package com.example.spender.domain.domainmodel.user

import com.google.firebase.firestore.DocumentReference

data class Friend(
    val name: UserName,
    val nickname: String,
    val docRef: DocumentReference
)