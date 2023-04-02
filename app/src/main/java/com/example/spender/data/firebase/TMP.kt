package com.example.spender.data.firebase

import com.example.spender.data.firebase.fieldNames.CollectionNames
import com.example.spender.data.firebase.fieldNames.CollectionTripDocumentFieldNames
import com.example.spender.data.firebase.fieldNames.CollectionUserDocumentFieldNames
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class TripManager {
    private val db by lazy { FirebaseFirestore.getInstance() }

    // Updates

    fun updateTripName(
        tripDocRef: DocumentReference,
        newName: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        tripDocRef.update(CollectionTripDocumentFieldNames.NAME, newName)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Deletes

    fun deleteTrip(
        tripDocRef: DocumentReference,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val batch = db.batch()

        getTripMembers(
            tripDocRef,
            { members ->
                members.forEach { member ->
                    batch.update(
                        member,
                        CollectionUserDocumentFieldNames.TRIPS,
                        FieldValue.arrayRemove(tripDocRef)
                    )
                }
            },
            { e ->
                onFailure(e)
            }
        )

        batch.delete(tripDocRef)

        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
