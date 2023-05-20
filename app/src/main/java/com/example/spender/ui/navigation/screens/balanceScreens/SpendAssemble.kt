package com.example.spender.ui.navigation.screens.balanceScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.spender.domain.remotemodel.LocalTrip
import com.example.spender.domain.remotemodel.spend.LocalSpend
import com.example.spender.domain.remotemodel.spendmember.LocalSpendMember
import com.example.spender.domain.remotemodel.user.toFriend
import com.google.firebase.firestore.GeoPoint

class SpendAssemble private constructor() {
    var name by mutableStateOf("")
    var totalSpend by mutableStateOf("")
    var category by mutableStateOf("")
    var splitMode by mutableStateOf(SplitMode.Custom)
    var latitude = 0.0
    var longitude = 0.0

    var members = mutableMapOf<String, Double>()

    private fun flushPreviousStates() {
        name = ""
        totalSpend = ""
        category = ""
        splitMode = SplitMode.Custom
        latitude = 0.0
        longitude = 0.0
    }

    fun initializeNewAssemble(trip: LocalTrip) {
        flushPreviousStates()
        members = buildMap {
            trip.members.forEach { friend ->
                this[friend.nickname] = 0.0
            }
        }.toMutableMap()
    }

    fun checkIfFieldsAreInitialized(): Boolean {
        return (name.isNotBlank() && totalSpend.isNotBlank() && members.isNotEmpty())
    }

    fun resetEqualSplitModeOnUserInput() {
        if (this.splitMode == SplitMode.Equal) {
            this.splitMode = SplitMode.Custom
        }
    }

    fun getEqualPayment(): String {
        return if (this.totalSpend.isNotBlank() && this.splitMode == SplitMode.Equal) {
            (this.totalSpend.toDouble() / this.members.size).toString()
        } else {
            "0"
        }
    }

    companion object {
        @Volatile
        private var instance: SpendAssemble? = null

        @Synchronized
        fun getInstance(): SpendAssemble {
            if (instance == null) {
                instance = SpendAssemble()
            }
            return instance as SpendAssemble
        }

        enum class SplitMode(s: String) {
            Equal("Equal"),
            Custom("Custom"),
        }
    }
}

fun SpendAssemble.toLocalSpend(trip: LocalTrip): LocalSpend {
    return LocalSpend(
        name = this.name,
        category = this.category,
        splitMode = this.splitMode.name,
        amount = this.totalSpend.toDouble(),
        geoPoint = GeoPoint(this.latitude, this.longitude),
        members = buildList {
            trip.members.forEach { friend ->
                this@buildList.add(
                    LocalSpendMember(
                        friend = friend.toFriend(),
                        payment = this@toLocalSpend.members[friend.nickname]!!,
                        debt = emptyList()
                    )
                )
            }
        }
    )
}