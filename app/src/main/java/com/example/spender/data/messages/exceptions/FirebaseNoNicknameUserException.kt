package com.example.spender.data.messages.exceptions

class FirebaseNoNicknameUserException: Exception() {
    override val message = "There is no user with this nickname"
}