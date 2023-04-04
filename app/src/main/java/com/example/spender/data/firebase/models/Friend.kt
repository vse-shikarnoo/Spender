package com.example.spender.data.firebase.models

import com.google.firebase.firestore.DocumentReference

data class Friend(
    val name: Triple<String, String, String>,
    val nickname: String,
    val docRef: DocumentReference
)
