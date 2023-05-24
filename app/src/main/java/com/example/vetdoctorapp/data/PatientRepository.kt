package com.example.vetdoctorapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

class PatientRepository(auth : FirebaseAuth) {
    private val ref = auth.currentUser?.let { References(it.uid) }

    fun getPatientData(
        patientId : String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        if (ref == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        ref.patientColRef.document(patientId).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

}