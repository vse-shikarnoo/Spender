package com.example.spender.domain.remotemodel.spendmember

import com.example.spender.domain.remotemodel.user.Friend
import com.google.firebase.firestore.DocumentReference

data class RemoteSpendMember(
    val friend: Friend, // DocumentReference
    val payment: Double,
    val debt: List<DebtToUser>,
    val docRef: DocumentReference
)
