package com.example.spender.domain.remotemodel

import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.LocalFriend
import com.example.spender.domain.remotemodel.user.toLocalFriend
import com.example.spender.domain.remotemodel.user.toFriend
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.Serializable

data class Trip(
    val name: String,
    val creator: Friend,
    val members: List<Friend>,
    // NOT USED val remoteSpends: List<RemoteSpend>,
    val docRef: DocumentReference
)

fun Trip.toLocalTrip(): LocalTrip {
    return LocalTrip(
        this.name,
        this.creator.toLocalFriend(),
        buildList {
            this@toLocalTrip.members.forEach {
                add(it.toLocalFriend())
            }
        },
        this.docRef.path
    )
}

@Serializable
data class LocalTrip(
    val name: String,
    val creator: LocalFriend,
    val members: List<LocalFriend>,
    val docRefPath: String
)

fun LocalTrip.toTrip(): Trip {
    return Trip(
        this.name,
        this.creator.toFriend(),
        buildList {
            this@toTrip.members.forEach {
                add(it.toFriend())
            }
        },
        FirebaseFirestore.getInstance().document(this.docRefPath)
    )
}
