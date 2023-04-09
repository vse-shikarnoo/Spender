package com.example.spender.data.firebase.messages.exceptions

class FirebaseNoUserSignedInException: Exception() {
    override val message = "No user signed in"
}