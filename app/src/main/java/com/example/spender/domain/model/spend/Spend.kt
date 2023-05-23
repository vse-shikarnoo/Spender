package com.example.spender.domain.remotemodel.spend

import com.example.spender.domain.remotemodel.spendmember.LocalSpendMember
import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
import com.example.spender.domain.remotemodel.spendmember.toLocalSpendMember
import com.google.firebase.firestore.GeoPoint
import kotlinx.serialization.Serializable

data class Spend(
    val name: String,
    val category: String,
    val splitMode: String,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<RemoteSpendMember>
)

fun Spend.toLocalSpend(): LocalSpend {
    return LocalSpend(
        this.name,
        this.category,
        this.splitMode,
        this.amount,
        this.geoPoint,
        buildList {
            this@toLocalSpend.members.forEach {
                add(it.toLocalSpendMember())
            }
        }
    )
}

data class LocalSpend(
    val name: String,
    val category: String,
    val splitMode: String,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<LocalSpendMember>
)

data class GoogleMapsSpend(
    val name: String,
    val latitude: Double,
    val longitude: Double
)
