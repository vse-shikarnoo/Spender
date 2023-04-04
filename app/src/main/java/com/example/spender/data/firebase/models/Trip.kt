package com.example.spender.data.firebase.models

import com.google.firebase.firestore.DocumentReference

data class Trip(
    val name: String,
    val creator: DocumentReference,
    val members: List<DocumentReference>,
    val spends: List<Spend>,
    val docRef: DocumentReference
)
