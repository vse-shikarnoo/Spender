package com.example.spender.data

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
//    val avatar: Bitmap,
//    @Embedded
//    val user_address: UserAddress
)

//data class UserAddress(
//    val street: String
//)