package com.example.spender.data.messages.exceptions

class FirebaseAlreadySentFriendException: Exception() {
    override val message = "Already sent a friend request"
}