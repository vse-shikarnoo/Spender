package com.example.spender.domain.remotemodel.user

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.Serializable

data class Friend(
    val name: UserName,
    val nickname: String,
    val docRef: DocumentReference
)

fun Friend.toLocalFriend(): LocalFriend {
    return LocalFriend(
        this.name,
        this.nickname,
        this.docRef.path
    )
}

@Serializable
data class LocalFriend(
    val name: UserName,
    val nickname: String,
    val docRefPath: String
)

fun LocalFriend.toFriend(): Friend {
    return Friend(
        this.name,
        this.nickname,
        FirebaseFirestore.getInstance().document(this.docRefPath)
    )
}
