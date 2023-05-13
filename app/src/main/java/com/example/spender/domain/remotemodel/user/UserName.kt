package com.example.spender.domain.remotemodel.user

import kotlinx.serialization.Serializable

@Serializable
data class UserName(
    val firstName: String,
    val middleName: String,
    val lastName: String,
)
