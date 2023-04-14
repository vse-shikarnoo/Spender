package com.example.spender.data.messages.exceptions

class FirebaseNicknameLengthException: Exception() {
    override val message = "Nickname must be at least 6 characters long"
}