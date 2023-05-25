package com.example.vetdoctorapp.model.repositories

import com.example.vetdoctorapp.model.data.Appointment
import com.example.vetdoctorapp.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

object PatientRepository{

    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    fun getPatientData(
        patientId : String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        db.collection(References.PATIENT_COL).document(patientId).get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun getQueue(
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val uid = auth.uid
        if (uid == null) {
            onFailure(Exception("User Not Logged In"))
            return
        }

        db.collection(References.USER_COL).document(uid).get()
            .addOnSuccessListener { user ->
                val queue = user.toObject<User>()?.queue
                if (queue != null) {
                    db.collection(References.APPOINT_COL)
                        .whereIn("id", queue).get()
                        .addOnSuccessListener { appointments ->
                            onSuccess(appointments.toObjects())
                        }
                }
            }
            .addOnFailureListener(onFailure)
    }
}