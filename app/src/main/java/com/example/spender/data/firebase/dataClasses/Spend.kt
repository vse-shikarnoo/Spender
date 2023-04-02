package com.example.spender.data.firebase.dataClasses

import com.google.firebase.firestore.DocumentReference

data class Spend(
    val name: String,
    val category: String,
    val amount: Int,
    val docRef: DocumentReference
)
