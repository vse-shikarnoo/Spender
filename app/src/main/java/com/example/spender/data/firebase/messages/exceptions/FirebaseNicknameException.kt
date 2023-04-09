package com.example.spender.data.firebase.messages.exceptions

class FirebaseNicknameException: Exception() {
    override val message = "Nickname is taken"
}
