package com.example.spender.data.messages.exceptions

class FirebaseNicknameException: Exception() {
    override val message = "Nickname is taken"
}
