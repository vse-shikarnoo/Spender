package com.example.spender.data.firebase

import com.example.spender.data.firebase.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseInstanceHolder {
    val db by lazy { FirebaseFirestore.getInstance() }
    val auth by lazy { FirebaseAuth.getInstance() }
}