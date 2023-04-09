package com.example.spender.data.firebase.messages.exceptions

class FirebaseNicknameLengthException: Exception() {
    override val message = "Nickname must be at least 6 characters long"
}