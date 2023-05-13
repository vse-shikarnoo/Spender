package com.example.spender.domain.remotemodel

import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.LocalFriend
import com.example.spender.domain.remotemodel.user.toLocalTrip
import com.example.spender.domain.remotemodel.user.toTrip
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
        this.creator.toLocalTrip(),
        buildList {
            this@toLocalTrip.members.forEach {
                add(it.toLocalTrip())
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
        this.creator.toTrip(),
        buildList {
            this@toTrip.members.forEach {
                add(it.toTrip())
            }
        },
        FirebaseFirestore.getInstance().document(this.docRefPath)
    )
}
